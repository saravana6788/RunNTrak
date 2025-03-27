package com.skcodes.core.domain.run

import kotlin.time.Duration

interface SyncRunScheduler {

    suspend fun scheduleSync(syncType:SyncType)
    suspend fun cancelAllSyncs()

    interface SyncType{
        data class FetchRun(val interval:Duration):SyncType
        data class CreateRun(val run:Run, val mapPicInBytes:ByteArray):SyncType
        data class DeleteRun(val runId:String):SyncType
    }
}