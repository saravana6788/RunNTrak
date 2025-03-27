package com.skcodes.run.data

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.await
import com.skcodes.core.database.dao.RunPendingSyncDao
import com.skcodes.core.database.entity.DeletedRunSyncEntity
import com.skcodes.core.database.entity.RunPendingSyncEntity
import com.skcodes.core.database.mapper.toRunEntity
import com.skcodes.core.domain.SessionStorage
import com.skcodes.core.domain.run.Run
import com.skcodes.core.domain.run.SyncRunScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.toJavaDuration

class SyncRunWorkScheduler(
    val context: Context,
    val runPendingSyncDao: RunPendingSyncDao,
    val sessionStorage: SessionStorage,
    val applicationScope:CoroutineScope
): SyncRunScheduler {

    private val workManager = WorkManager.getInstance(context)

    override suspend fun scheduleSync(syncType: SyncRunScheduler.SyncType) {
        when(syncType){
            is SyncRunScheduler.SyncType.FetchRun -> scheduleFetchRunsWorker(syncType.interval)
            is SyncRunScheduler.SyncType.CreateRun -> scheduleCreateRunWorker(syncType.run,syncType.mapPicInBytes)
            is SyncRunScheduler.SyncType.DeleteRun -> scheduleDeleteRunWorker(syncType.runId)
        }
    }

    override suspend fun cancelAllSyncs() {
        workManager.cancelAllWork().await()
    }


    private suspend fun scheduleFetchRunsWorker(interval: Duration){
        // check if there is any fetch run scheduler that is already running.
        val isPendingFetchRunSync = workManager.getWorkInfosByTag("sync_work").get().isNotEmpty()
        //if already running do not run it again
        if(isPendingFetchRunSync)
            return

        val workRequest = PeriodicWorkRequestBuilder<FetchRunWorker>(
            interval.toJavaDuration()
        ).setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            ).setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .setInitialDelay(
                duration = 30,
                timeUnit = TimeUnit.MINUTES
            )
            .addTag("sync_work")
            .build()

        workManager.enqueue(workRequest).await()

    }


    private suspend fun scheduleCreateRunWorker(run: Run, mapPicInBytes:ByteArray){
        // upsert the run into the RunPendingSync DB so that the mapPic can be retrived by the worker
        val userId = sessionStorage.get()?.userId ?: return
        val runPendingSyncEntity = RunPendingSyncEntity(
            run = run.toRunEntity(),
            userId = userId,
            mapPicInBytes = mapPicInBytes,
        )

        runPendingSyncDao.upsertRunPendingSyncEntity(runPendingSyncEntity)

        val workRequest = OneTimeWorkRequestBuilder<CreateRunWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .setInputData(
                Data.Builder()
                    .putString(CreateRunWorker.RUN_ID,runPendingSyncEntity.runId)
                    .build()
            )
            .addTag("create_work")
            .build()

        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()

    }


    private suspend fun scheduleDeleteRunWorker(runId:String){

        val userId = sessionStorage.get()?.userId?:return

        val deletedRunSyncEntity = DeletedRunSyncEntity(
            runId = runId,
            userId = userId
        )

        runPendingSyncDao.upsertDeletedRunSyncEntity(deletedRunSyncEntity)

        val workRequest = OneTimeWorkRequestBuilder<DeleteRunWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .setInputData(
                Data.Builder().putString(DeleteRunWorker.RUN_ID,runId)
                    .build()
            )
            .addTag("delete_work")
            .build()

        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()

    }
}