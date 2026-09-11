package com.example.evofit.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

@Entity(tableName = "weight_history")
data class WeightUpdateEntity(
    @PrimaryKey
    @get:PropertyName("id") @set:PropertyName("id") @PropertyName("id")
    var id: String = "",
    
    @get:PropertyName("userId") @set:PropertyName("userId") @PropertyName("userId")
    var userId: String = "",
    
    @get:PropertyName("weight") @set:PropertyName("weight") @PropertyName("weight")
    var weight: String = "",
    
    @get:PropertyName("date") @set:PropertyName("date") @PropertyName("date")
    var date: String = "",
    
    @get:PropertyName("timestamp") @set:PropertyName("timestamp") @PropertyName("timestamp")
    var timestamp: Long = System.currentTimeMillis(),

    @get:PropertyName("isDeleted") @set:PropertyName("isDeleted") @PropertyName("isDeleted")
    var isDeleted: Boolean = false,

    @Exclude
    var syncStatus: SyncStatus = SyncStatus.SYNCED
)
