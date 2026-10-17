package com.guaracode.evofit.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weight_history")
data class WeightUpdateEntity(
    @PrimaryKey
    var id: String = "",
    var userId: String = "",
    var weight: String = "",
    var date: String = "",
    var timestamp: Long = kotlinx.datetime.Clock.System.now().toEpochMilliseconds(),
    var isDeleted: Boolean = false,
    var syncStatus: SyncStatus = SyncStatus.SYNCED
)
