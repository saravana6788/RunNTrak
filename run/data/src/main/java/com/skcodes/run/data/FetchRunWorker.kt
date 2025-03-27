package com.skcodes.run.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.skcodes.core.domain.run.RunRepository
import com.skcodes.core.domain.util.DataError


// Worker that frequently syncs the runs from network
class FetchRunWorker(
    context: Context,
    params: WorkerParameters,
    private val runRepository: RunRepository
):CoroutineWorker(context,params) {
    override suspend fun doWork(): Result {
        if(runAttemptCount >= 5){
            return Result.failure()
        }

       return when(val result = runRepository.fetchRuns()){
            is com.skcodes.core.domain.util.Result.Error -> {
                result.error.toWorkerResult()
            }
            is com.skcodes.core.domain.util.Result.Success -> {
                Result.success()
            }
        }
    }

}