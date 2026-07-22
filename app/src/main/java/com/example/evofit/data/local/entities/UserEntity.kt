package com.example.evofit.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.PropertyName

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String = "",
    val name: String = "",
    val age: String = "",
    val weight: String = "",
    val height: String = "",
    
    @get:PropertyName("onboardingCompleted")
    @set:PropertyName("onboardingCompleted")
    @PropertyName("onboardingCompleted")
    @ColumnInfo(name = "isOnboardingCompleted")
    var onboardingCompleted: Boolean = false,
    
    val updatedAt: Long = 0L
)
