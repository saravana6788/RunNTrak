package com.skcodes.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.skcodes.core.database.entity.RunEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {

    @Upsert
    suspend fun upsertRun(run:RunEntity)

    @Upsert
    suspend fun upsertRuns(runs:List<RunEntity>)

    @Query("SELECT * FROM RunEntity ORDER BY dateTImeUtc DESC")
    fun getRuns(): Flow<List<RunEntity>>

    @Query("DELETE FROM RunEntity WHERE id=:id")
    suspend fun deleteRun(id:String)

    @Query("DELETE FROM RunEntity")
    suspend fun deleteAllRuns()
}