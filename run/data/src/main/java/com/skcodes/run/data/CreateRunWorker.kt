package com.skcodes.run.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.skcodes.core.database.dao.RunPendingSyncDao
import com.skcodes.core.database.mapper.toRun
import com.skcodes.core.domain.run.RemoteRunDataSource
import com.skcodes.core.domain.util.Result

class CreateRunWorker(
    context: Context,
    private val params: WorkerParameters,
    private val remoteRunDataSource: RemoteRunDataSource,
    private val runPendingSyncDao: RunPendingSyncDao
) : CoroutineWorker(context,params){
    override suspend fun doWork(): Result {
        if(runAttemptCount >= 5){
            return  Result.failure()
        }

        val pendingRunId = params.inputData.getString(RUN_ID)?:return Result.failure()

        val pendingRunFromSync = runPendingSyncDao.getRunPendingSyncEntity(pendingRunId)?:return Result.failure()
        val pendingRun = pendingRunFromSync.run.toRun()
        return when(val result = remoteRunDataSource.postRun(
            run = pendingRun,
            mapPicture = pendingRunFromSync.mapPicInBytes
        ) ){
            is com.skcodes.core.domain.util.Result.Error -> { result.error.toWorkerResult() }
            is com.skcodes.core.domain.util.Result.Success -> {
                runPendingSyncDao.deleteRunPendingSyncEntity(pendingRunId)
                Result.success()
            }
        }
    }

    companion object{
        const val RUN_ID = "RUN_ID"
    }

}