# Bug Fix: Workout Elements Out of Order after App Restart

This plan addresses the issue where muscle groups (exercises) and sets appear out of order in `WorkoutStartScreen` after the app is restarted while a workout is in progress.

## Root Cause Analysis

1.  **Missing `orderIndex` in `WorkoutExerciseEntity`**: Exercises within a workout are stored in `workout_exercises` table without an explicit order column. When retrieved via Room `@Relation` in `FullWorkout`, the order is not guaranteed. Since muscle groups are grouped by exercises in the UI, they also appear out of order.
2.  **Unsorted Sets**: `ExerciseSetEntity` has a `setNumber` column, but it is not used for sorting when converting the entity to the domain model in `WorkoutMapper.kt`.
3.  **Incorrect Set Mapping in ViewModel**: `WorkoutStartViewModel.kt` reassigns `setNumber` based on the list index (`index + 1`) instead of using the `setNumber` from the domain model. If the list is out of order, the data (weight/reps) will be associated with the wrong set number.

## Proposed Changes

### 1. Domain Layer

#### [MODIFY] [WorkoutExercise.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/domain/model/WorkoutExercise.kt)
- Add `val orderIndex: Int = 0` to the `WorkoutExercise` data class.

### 2. Data Layer

#### [MODIFY] [WorkoutExerciseEntity.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/data/local/entities/WorkoutExerciseEntity.kt)
- Add `val orderIndex: Int = 0` to the `WorkoutExerciseEntity` data class.

#### [MODIFY] [WorkoutMapper.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/data/mapper/WorkoutMapper.kt)
- Update `FullWorkout.toDomain`: Sort `exercises` by `workoutExercise.orderIndex`.
- Update `WorkoutExerciseWithSets.toDomain`: Sort `sets` by `setNumber`.
- Update `WorkoutExercise.toEntity`: Pass `orderIndex` to the entity.

#### [MODIFY] [AppDatabase.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/data/local/AppDatabase.kt)
- Increment `version` to `8`.
- Add `MIGRATION_7_8` to add `orderIndex` column to `workout_exercises` table.

### 3. Presentation Layer

#### [MODIFY] [ConfigureWorkoutViewModel.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/workout/createworkout/viewmodel/ConfigureWorkoutViewModel.kt)
- In `buildMuscleGroupAndExercises`, use `mapIndexed` to set `orderIndex` for each `WorkoutExercise` based on its position in the UI list.

#### [MODIFY] [WorkoutStartViewModel.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/workout/startworkout/viewmodel/WorkoutStartViewModel.kt)
- In `loadWorkout`, use `set.setNumber` instead of `index + 1` when creating `SetProgressState`.

## Verification Plan

### Automated Tests
- I will check if there are existing tests for `WorkoutMapper` and add tests for sorting logic.
- Run `gradlew test` (or relevant test task) to ensure no regressions.

### Manual Verification
1.  Open the app and create a workout with multiple muscle groups (e.g., Chest then Arms).
2.  Start the workout in `WorkoutStartScreen`.
3.  Check the order of muscle groups and sets.
4.  Kill the app.
5.  Reopen the app, navigate to `WorkoutScreen`, and resume the workout.
6.  Verify that the order of muscle groups and sets remains identical to step 3.
7.  Verify that "done" status of sets is preserved correctly.
