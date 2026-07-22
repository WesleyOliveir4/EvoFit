package com.example.evofit.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_goals",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId"])]
)
data class UserGoalEntity(
    @PrimaryKey val id: String = "",
    val userId: String = "",
    val type: String = "",
    val exerciseName: String? = null,
    val value: String? = null,
    val unit: String? = null,
    val cardioType: String? = null,
    val distance: String? = null,
    val time: String? = null
)
