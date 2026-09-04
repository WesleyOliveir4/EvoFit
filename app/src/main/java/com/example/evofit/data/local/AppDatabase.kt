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
        ActiveSessionSetEntity::class,
        WorkoutDoneEntity::class
    ],
    version = 11,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        val MIGRATION_10_11 = object : Migration(10, 11) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Change Primary Key of exercise_sets from 'id' to (workoutExerciseId, setNumber)
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS exercise_sets_new (
                        id TEXT NOT NULL,
                        workoutExerciseId TEXT NOT NULL,
                        setNumber INTEGER NOT NULL,
                        reps INTEGER NOT NULL,
                        load REAL NOT NULL,
                        unit TEXT NOT NULL,
                        time INTEGER,
                        distance REAL,
                        PRIMARY KEY(workoutExerciseId, setNumber),
                        FOREIGN KEY(workoutExerciseId) REFERENCES workout_exercises(id) ON UPDATE NO ACTION ON DELETE CASCADE 
                    )
                """)
                
                // Copy data (ignoring duplicates if any existed in a broken state)
                database.execSQL("""
                    INSERT OR IGNORE INTO exercise_sets_new (id, workoutExerciseId, setNumber, reps, load, unit, time, distance)
                    SELECT id, workoutExerciseId, setNumber, reps, load, unit, time, distance FROM exercise_sets
                """)
                
                database.execSQL("DROP TABLE exercise_sets")
                database.execSQL("ALTER TABLE exercise_sets_new RENAME TO exercise_sets")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_exercise_sets_workoutExerciseId ON exercise_sets(workoutExerciseId)")
            }
        }

        val MIGRATION_9_10 = object : Migration(9, 10) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 1. Workouts: Drop muscleGroupId
                database.execSQL("""
                    CREATE TABLE workouts_new (
                        workoutId TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        name TEXT NOT NULL,
                        date TEXT NOT NULL,
                        orderIndex INTEGER NOT NULL DEFAULT 0,
                        updatedAt INTEGER NOT NULL DEFAULT 0,
                        FOREIGN KEY(userId) REFERENCES users(id) ON UPDATE NO ACTION ON DELETE CASCADE 
                    )
                """)
                database.execSQL("""
                    INSERT INTO workouts_new (workoutId, userId, name, date, orderIndex, updatedAt)
                    SELECT workoutId, userId, name, date, orderIndex, updatedAt FROM workouts
                """)
                database.execSQL("DROP TABLE workouts")
                database.execSQL("ALTER TABLE workouts_new RENAME TO workouts")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_workouts_userId ON workouts(userId)")

                // 2. WorkoutExercises: Add muscleGroupId
                database.execSQL("""
                    CREATE TABLE workout_exercises_new (
                        id TEXT NOT NULL PRIMARY KEY,
                        workoutId TEXT NOT NULL,
                        exerciseId TEXT NOT NULL,
                        muscleGroupId TEXT NOT NULL DEFAULT '',
                        orderIndex INTEGER NOT NULL DEFAULT 0,
                        FOREIGN KEY(workoutId) REFERENCES workouts(workoutId) ON UPDATE NO ACTION ON DELETE CASCADE 
                    )
                """)
                database.execSQL("""
                    INSERT INTO workout_exercises_new (id, workoutId, exerciseId, muscleGroupId, orderIndex)
                    SELECT id, workoutId, exerciseId, '', orderIndex FROM workout_exercises
                """)
                database.execSQL("DROP TABLE workout_exercises")
                database.execSQL("ALTER TABLE workout_exercises_new RENAME TO workout_exercises")
                database.execSQL("CREATE INDEX IF NOT EXISTS index_workout_exercises_workoutId ON workout_exercises(workoutId)")

                // 3. WorkoutDone: Drop muscleGroupId, change exercises to exercisesByGroup
                database.execSQL("""
                    CREATE TABLE workout_done_new (
                        id TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        name TEXT NOT NULL,
                        date TEXT NOT NULL,
                        exercisesByGroup TEXT NOT NULL,
                        time TEXT NOT NULL,
                        createdAt INTEGER NOT NULL
                    )
                """)
                // For simplicity, we initialize exercisesByGroup as empty or we could try to map, 
                // but since the format changes significantly, empty is safer for schema migration.
                database.execSQL("""
                    INSERT INTO workout_done_new (id, userId, name, date, exercisesByGroup, time, createdAt)
                    SELECT id, userId, name, date, '[]', time, createdAt FROM workout_done
                """)
                database.execSQL("DROP TABLE workout_done")
                database.execSQL("ALTER TABLE workout_done_new RENAME TO workout_done")
            }
        }

        val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS workout_done (
                        id TEXT NOT NULL PRIMARY KEY,
                        userId TEXT NOT NULL,
                        name TEXT NOT NULL,
                        muscleGroupId TEXT NOT NULL,
                        date TEXT NOT NULL,
                        exercises TEXT NOT NULL,
                        time TEXT NOT NULL,
                        createdAt INTEGER NOT NULL
                    )
                """)
            }
        }

        val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE workout_exercises ADD COLUMN orderIndex INTEGER NOT NULL DEFAULT 0")
            }
        }

        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Rename age to birthDate in users table
                // Safe way for older SQLite versions:
                database.execSQL("""
                    CREATE TABLE users_new (
                        id TEXT NOT NULL PRIMARY KEY,
                        name TEXT NOT NULL,
                        birthDate TEXT NOT NULL,
                        weight TEXT NOT NULL,
                        height TEXT NOT NULL,
                        isOnboardingCompleted INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                """)
                database.execSQL("""
                    INSERT INTO users_new (id, name, birthDate, weight, height, isOnboardingCompleted, updatedAt)
                    SELECT id, name, age, weight, height, isOnboardingCompleted, updatedAt FROM users
                """)
                database.execSQL("DROP TABLE users")
                database.execSQL("ALTER TABLE users_new RENAME TO users")
            }
        }

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
