package com.skcodes.core.database

import androidx.room.Database
import androidx.room.Entity
import androidx.room.RoomDatabase
import com.skcodes.core.database.dao.AnalyticsDao
import com.skcodes.core.database.dao.RunDao
import com.skcodes.core.database.dao.RunPendingSyncDao
import com.skcodes.core.database.entity.DeletedRunSyncEntity
import com.skcodes.core.database.entity.RunEntity
import com.skcodes.core.database.entity.RunPendingSyncEntity

@Database(
    entities = [RunEntity::class,
               RunPendingSyncEntity::class,
               DeletedRunSyncEntity::class],
    version = 1
)
abstract class RunsDatababse:RoomDatabase() {
    abstract val runDao:RunDao
    abstract val analyticsDao:AnalyticsDao
    abstract val runPendingSyncDao:RunPendingSyncDao
}
