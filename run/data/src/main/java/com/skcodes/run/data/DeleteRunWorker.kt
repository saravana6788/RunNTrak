package com.skcodes.run.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.skcodes.core.database.dao.RunPendingSyncDao
import com.skcodes.core.domain.run.RemoteRunDataSource
import com.skcodes.core.domain.run.RunRepository
import com.skcodes.core.domain.util.Result

class DeleteRunWorker(
    val context: Context,
    val params: WorkerParameters,
    private val remoteRunDataSource: RemoteRunDataSource,
    private val pendingSyncDao: RunPendingSyncDao
):CoroutineWorker(context,params) {
    override suspend fun doWork(): Result {
        if(runAttemptCount >= 5){
            return Result.failure()
        }
        val runId = params.inputData.getString(RUN_ID)?: return Result.failure()
        return  when(val result = remoteRunDataSource.deleteRun(runId)){
             is com.skcodes.core.domain.util.Result.Error -> result.error.toWorkerResult()
             is com.skcodes.core.domain.util.Result.Success -> {
                 pendingSyncDao.deleteDeletedRunSyncEntity(runId)
                 Result.success()
             }
         }
    }


    companion object{
        const val RUN_ID = "RUN_ID"
    }

}