package com.guaracode.evofit.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.guaracode.evofit.data.local.dao.UserDao
import com.guaracode.evofit.data.local.dao.WeightHistoryDao
import com.guaracode.evofit.data.local.entities.*

@Database(
    entities = [
        UserEntity::class,
        UserGoalEntity::class,
        WorkoutEntity::class,
        WorkoutExerciseEntity::class,
        ExerciseSetEntity::class,
        WorkoutDoneEntity::class,
        WorkoutDoneHistoryEntity::class,
        ActiveSessionEntity::class,
        ActiveSessionSetEntity::class,
        WeightUpdateEntity::class
    ],
    version = 13,
    exportSchema = false
)
@TypeConverters(Converters::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun weightHistoryDao(): WeightHistoryDao
}

expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>
