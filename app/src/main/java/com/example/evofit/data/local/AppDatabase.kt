package com.example.evofit.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.evofit.data.local.dao.UserDao
import com.example.evofit.data.local.entities.*

@Database(
    entities = [
        UserEntity::class,
        UserGoalEntity::class,
        WorkoutEntity::class,
        WorkoutExerciseEntity::class,
        ExerciseSetEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
