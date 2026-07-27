package com.example.evofit.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.PropertyName

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey 
    @get:PropertyName("id") @set:PropertyName("id") @PropertyName("id")
    var id: String = "",

    @get:PropertyName("name") @set:PropertyName("name") @PropertyName("name")
    var name: String = "",

    @ColumnInfo(name = "birthDate")
    @get:PropertyName("age") @set:PropertyName("age") @PropertyName("age")
    var birthDate: String = "",

    @get:PropertyName("weight") @set:PropertyName("weight") @PropertyName("weight")
    var weight: String = "",

    @get:PropertyName("height") @set:PropertyName("height") @PropertyName("height")
    var height: String = "",
    
    @get:PropertyName("onboardingCompleted")
    @set:PropertyName("onboardingCompleted")
    @PropertyName("onboardingCompleted")
    @ColumnInfo(name = "isOnboardingCompleted")
    var onboardingCompleted: Boolean = false,
    
    @get:PropertyName("updatedAt") @set:PropertyName("updatedAt") @PropertyName("updatedAt")
    var updatedAt: Long = 0L
)
