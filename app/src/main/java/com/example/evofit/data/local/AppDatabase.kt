package com.example.evofit.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.evofit.data.local.dao.UserDao
import com.example.evofit.data.local.entities.*

@Database(
    entities = [
        UserEntity::class,
        UserGoalEntity::class,
        WorkoutEntity::class,
        WorkoutExerciseEntity::class,
        ExerciseSetEntity::class,
        WorkoutDoneHistoryEntity::class,
        ActiveSessionEntity::class,
        ActiveSessionSetEntity::class
    ],
    version = 6,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE active_session (
                        workoutId TEXT NOT NULL PRIMARY KEY,
                        startTime INTEGER NOT NULL
                    )
                """)
                database.execSQL("""
                    CREATE TABLE active_session_sets (
                        id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                        workoutId TEXT NOT NULL,
                        workoutExerciseId TEXT NOT NULL,
                        setNumber INTEGER NOT NULL,
                        FOREIGN KEY(workoutId) REFERENCES active_session(workoutId) ON UPDATE NO ACTION ON DELETE CASCADE 
                    )
                """)
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 1. UserEntity: Adicionar updatedAt
                database.execSQL("ALTER TABLE users ADD COLUMN updatedAt INTEGER NOT NULL DEFAULT 0")

                // 2. WorkoutEntity: Mudar workoutId de INTEGER para TEXT
                database.execSQL("""
                    CREATE TABLE workouts_new (
                        workoutId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        name TEXT NOT NULL,
                        muscleGroupId TEXT NOT NULL,
                        date TEXT NOT NULL,
                        orderIndex INTEGER NOT NULL DEFAULT 0,
                        updatedAt INTEGER NOT NULL DEFAULT 0,
                        FOREIGN KEY(userId) REFERENCES users(id) ON UPDATE NO ACTION ON DELETE CASCADE 
                    )
                """)
                database.execSQL("""
                    INSERT INTO workouts_new (workoutId, userId, name, muscleGroupId, date, orderIndex, updatedAt)
                    SELECT CAST(workoutId AS TEXT), userId, name, muscleGroupId, date, orderIndex, 0 FROM workouts
                """)
                database.execSQL("DROP TABLE workouts")
                database.execSQL("ALTER TABLE workouts_new RENAME TO workouts")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_workouts_userId ON workouts(userId)")

                // 3. WorkoutExerciseEntity: Mudar id e workoutId para TEXT
                database.execSQL("""
                    CREATE TABLE workout_exercises_new (
                        id TEXT NOT NULL PRIMARY KEY,
                        workoutId TEXT NOT NULL,
                        exerciseId TEXT NOT NULL,
                        FOREIGN KEY(workoutId) REFERENCES workouts(workoutId) ON UPDATE NO ACTION ON DELETE CASCADE 
                    )
                """)
                database.execSQL("""
                    INSERT INTO workout_exercises_new (id, workoutId, exerciseId)
                    SELECT CAST(id AS TEXT), CAST(workoutId AS TEXT), exerciseId FROM workout_exercises
                """)
                database.execSQL("DROP TABLE workout_exercises")
                database.execSQL("ALTER TABLE workout_exercises_new RENAME TO workout_exercises")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_workout_exercises_workoutId ON workout_exercises(workoutId)")

                // 4. ExerciseSetEntity: Mudar id e workoutExerciseId para TEXT
                database.execSQL("""
                    CREATE TABLE exercise_sets_new (
                        id TEXT NOT NULL PRIMARY KEY,
                        workoutExerciseId TEXT NOT NULL,
                        setNumber INTEGER NOT NULL,
                        reps INTEGER NOT NULL,
                        load REAL NOT NULL,
                        unit TEXT NOT NULL,
                        time INTEGER,
                        distance REAL,
                        FOREIGN KEY(workoutExerciseId) REFERENCES workout_exercises(id) ON UPDATE NO ACTION ON DELETE CASCADE 
                    )
                """)
                database.execSQL("""
                    INSERT INTO exercise_sets_new (id, workoutExerciseId, setNumber, reps, load, unit, time, distance)
                    SELECT CAST(id AS TEXT), CAST(workoutExerciseId AS TEXT), setNumber, reps, load, unit, time, distance FROM exercise_sets
                """)
                database.execSQL("DROP TABLE exercise_sets")
                database.execSQL("ALTER TABLE exercise_sets_new RENAME TO exercise_sets")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_exercise_sets_workoutExerciseId ON exercise_sets(workoutExerciseId)")
            }
        }
    }
}
