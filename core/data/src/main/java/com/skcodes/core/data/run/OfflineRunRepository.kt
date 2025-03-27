package com.skcodes.core.data.run

import com.skcodes.core.data.auth.EncryptedSessionStorage
import com.skcodes.core.data.networking.get
import com.skcodes.core.database.dao.RunPendingSyncDao
import com.skcodes.core.database.mapper.toRun
import com.skcodes.core.domain.SessionStorage
import com.skcodes.core.domain.run.LocalRunDataSource
import com.skcodes.core.domain.run.RemoteRunDataSource
import com.skcodes.core.domain.run.Run
import com.skcodes.core.domain.run.RunId
import com.skcodes.core.domain.run.RunRepository
import com.skcodes.core.domain.run.SyncRunScheduler
import com.skcodes.core.domain.util.DataError
import com.skcodes.core.domain.util.EmptyResult
import com.skcodes.core.domain.util.Result
import com.skcodes.core.domain.util.asEmptyDataResult
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin

import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OfflineRunRepository(
    private val remoteRunDataSource: RemoteRunDataSource,
    private val localRunDataSource: LocalRunDataSource,
    private val applicationScope:CoroutineScope,
    private val runPendingSyncDao: RunPendingSyncDao,
    private val sessionStorage: SessionStorage,
    private val syncRunScheduler: SyncRunScheduler,
    private val httpClient:HttpClient
) :RunRepository{
    override fun getRuns(): Flow<List<Run>> {
        return localRunDataSource.getRuns()
    }

    override suspend fun fetchRuns(): EmptyResult<DataError> {
        return when(val result = remoteRunDataSource.getRuns()){
            is Result.Error -> {
                result.asEmptyDataResult()
            }
            is Result.Success -> {
                applicationScope.async {
                    localRunDataSource.upsertRuns(result.data).asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun upsertRun(run: Run, mapPicture: ByteArray): EmptyResult<DataError> {
        val result = localRunDataSource.upsertRun(run)

        if(result !is Result.Success){
            return result.asEmptyDataResult()
        }
        val runWithId = run.copy( id = result.data)
        val remoteResult = remoteRunDataSource.postRun(
                run = runWithId,
                mapPicture = mapPicture,
            )

       return when(remoteResult){
            is Result.Error -> {
                applicationScope.launch {
                    syncRunScheduler.scheduleSync(
                        SyncRunScheduler.SyncType.CreateRun(
                            run = runWithId,
                            mapPicInBytes = mapPicture,
                        )
                    )
                }.join()
                Result.Success(Unit)
            }
            is Result.Success -> {
                applicationScope.async {
                    localRunDataSource.upsertRun(remoteResult.data).asEmptyDataResult()
                }.await()
            }
        }


    }

    override suspend fun deleteRun(id: RunId) {
        val result = localRunDataSource.deleteRun(id)

        // Edge case :: A run is created and deleted locally before going online
         val isPendingSync = runPendingSyncDao.getRunPendingSyncEntity(id) != null

        if(isPendingSync){
            runPendingSyncDao.deleteRunPendingSyncEntity(id)
            return
        }

       val remoteResult =  applicationScope.async {
            remoteRunDataSource.deleteRun(id)
        }.await()

        when(remoteResult){
            is Result.Error -> {
                applicationScope.launch {
                syncRunScheduler.scheduleSync(
                    SyncRunScheduler.SyncType.DeleteRun(id)
                )
                }.join()
            }
            is Result.Success -> {

            }
        }

    }

    override suspend fun syncPendingRuns() {
        withContext(Dispatchers.IO){
            val userId = sessionStorage.get()?.userId?: return@withContext

            val createdRuns = async {
                runPendingSyncDao.getAllRunPendingSyncEntities(userId)
            }

            val deletedRuns = async{
                runPendingSyncDao.getAllDeletedRunSyncEntities(userId)
            }

            val createdJobs = createdRuns.await()
                .map {
                    launch{
                        val run = it.run.toRun()
                        val result = remoteRunDataSource.postRun(
                            run  = run,
                            mapPicture = it.mapPicInBytes
                        )

                        when(result){
                            is Result.Error -> {

                            }
                            is Result.Success -> {
                                applicationScope.launch {
                                    runPendingSyncDao.deleteRunPendingSyncEntity(it.runId)
                                }.join()
                            }
                        }
                    }
                }

            val deleteJobs = deletedRuns
                .await()
                .map {
                    launch {
                        when(remoteRunDataSource.deleteRun(it.runId)){
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    runPendingSyncDao.deleteDeletedRunSyncEntity(it.runId)
                                }.join()
                            }
                        }
                    }
                }

            createdJobs.forEach { it.join() }
            deleteJobs.forEach { it.join() }

        }

    }

    override suspend fun deleteAllRuns() {
        localRunDataSource.deleteAllRuns()
    }


    override suspend fun logout():EmptyResult<DataError.NetworkError> {
        val result = httpClient.get<Unit>(
            route = "/logout"
        ).asEmptyDataResult()
        httpClient.plugin(Auth).providers.filterIsInstance<BearerAuthProvider>()
            .firstOrNull()
            ?.clearToken()
        return result
    }


}