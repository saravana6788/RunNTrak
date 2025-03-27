package com.skcodes.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.skcodes.core.database.entity.DeletedRunSyncEntity
import com.skcodes.core.database.entity.RunPendingSyncEntity

@Dao
interface RunPendingSyncDao {

    // FOR CREATED RUNS

    @Query("SELECT * from RunPendingSyncEntity where userId= :userId")
    suspend fun getAllRunPendingSyncEntities(userId:String):List<RunPendingSyncEntity>

    @Query("SELECT * FROM RunPendingSyncEntity where id = :runId")
    suspend fun getRunPendingSyncEntity(runId:String):RunPendingSyncEntity?

    @Query("DELETE FROM RunPendingSyncEntity where id = :runId")
    suspend fun deleteRunPendingSyncEntity(runId:String)

    @Upsert
    suspend fun upsertRunPendingSyncEntity(runPendingSyncEntity: RunPendingSyncEntity)


    //DELETED RUNS

    @Query("SELECT * FROM DeletedRunSyncEntity where userId = :userId")
    suspend fun getAllDeletedRunSyncEntities(userId: String):List<DeletedRunSyncEntity>

    @Upsert
    suspend fun upsertDeletedRunSyncEntity(deletedRunSyncEntity: DeletedRunSyncEntity)

    @Query("DELETE FROM DeletedRunSyncEntity where runId = :runId")
    suspend fun deleteDeletedRunSyncEntity(runId:String)
}