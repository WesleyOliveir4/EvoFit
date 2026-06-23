package com.example.evofit.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val age: String,
    val weight: String,
    val height: String,
    val isOnboardingCompleted: Boolean = false
)
