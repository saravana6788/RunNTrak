package com.skcodes.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RunPendingSyncEntity(
    @Embedded val run:RunEntity,
    @PrimaryKey(autoGenerate = false)
    val runId:String = run.id,
    val userId:String,
    val mapPicInBytes:ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RunPendingSyncEntity

        if (run != other.run) return false
        if (runId != other.runId) return false
        if (userId != other.userId) return false
        if (!mapPicInBytes.contentEquals(other.mapPicInBytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = run.hashCode()
        result = 31 * result + runId.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + mapPicInBytes.contentHashCode()
        return result
    }

}
