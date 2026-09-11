package com.example.evofit.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.evofit.domain.model.WorkoutGroup
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

@Entity(tableName = "workout_done")
data class WorkoutDoneEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val name: String,
    val date: String,
    val exercisesByGroup: List<WorkoutGroup>,
    val time: String,
    val createdAt: Long,

    @get:PropertyName("isDeleted") @set:PropertyName("isDeleted") @PropertyName("isDeleted")
    var isDeleted: Boolean = false,

    @Exclude
    var syncStatus: SyncStatus = SyncStatus.SYNCED
)
