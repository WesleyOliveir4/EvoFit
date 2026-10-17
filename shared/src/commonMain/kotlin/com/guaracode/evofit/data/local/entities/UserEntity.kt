package com.guaracode.evofit.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey 
    var id: String = "",
    var name: String = "",
    @ColumnInfo(name = "birthDate")
    var birthDate: String = "",
    var weight: String = "",
    var height: String = "",
    var profilePictureUri: String? = null,
    @ColumnInfo(name = "isOnboardingCompleted")
    var onboardingCompleted: Boolean = false,
    var updatedAt: Long = 0L,
    var syncStatus: SyncStatus = SyncStatus.SYNCED
)
