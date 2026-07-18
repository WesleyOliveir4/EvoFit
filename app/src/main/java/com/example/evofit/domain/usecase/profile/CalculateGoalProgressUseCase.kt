package com.example.evofit.domain.usecase.profile

import com.example.evofit.domain.model.UserGoal
import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.repository.OnboardingRepository
import com.example.evofit.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.firstOrNull

data class GoalProgress(
    val currentValue: Double,
    val targetValue: Double,
    val percentage: Int,
    val unit: String = ""
)

interface CalculateGoalProgressUseCase {
    suspend operator fun invoke(goal: UserGoal, userId: String): GoalProgress
}

class CalculateGoalProgressUseCaseImpl(
    private val workoutRepository: WorkoutRepository,
    private val onboardingRepository: OnboardingRepository
) : CalculateGoalProgressUseCase {

    override suspend fun invoke(goal: UserGoal, userId: String): GoalProgress {
        val history = workoutRepository.getWorkoutDoneHistory(userId)

        return when (goal) {
            is UserGoal.Strength -> calculateStrengthProgress(goal, history)
            is UserGoal.Cardio -> calculateCardioProgress(goal, history)
            is UserGoal.Weight -> calculateWeightProgress(goal)
        }
    }

    private fun calculateStrengthProgress(goal: UserGoal.Strength, history: List<WorkoutDone>): GoalProgress {
        val targetValue = goal.value.toDoubleOrNull() ?: 0.0
        if (targetValue <= 0) return GoalProgress(0.0, targetValue, 0)

        var bestValue = 0.0
        history.forEach { workout ->
            workout.exercises.forEach { exercise ->
                exercise.sets.forEach { set ->
                    if (set.exerciseName.equals(goal.exerciseName, ignoreCase = true)) {
                        val value = if (goal.unit.name == "REPS") set.reps.toDouble() else set.load
                        if (value > bestValue) bestValue = value
                    }
                }
            }
        }

        val percentage = if (targetValue > 0) (bestValue / targetValue * 100).toInt() else 0
        return GoalProgress(bestValue, targetValue, percentage.coerceIn(0, 1000))
    }

    private fun calculateCardioProgress(goal: UserGoal.Cardio, history: List<WorkoutDone>): GoalProgress {
        val targetDistance = goal.distance?.toDoubleOrNull() ?: 0.0
        val targetTime = goal.time.toDoubleOrNull() ?: 0.0

        var bestDistance = 0.0
        var bestTime = 0.0

        history.forEach { workout ->
            workout.exercises.forEach { exercise ->
                exercise.sets.forEach { set ->
                    if (set.exerciseName.equals(goal.type, ignoreCase = true)) {
                        set.distance?.let { if (it > bestDistance) bestDistance = it }
                        set.time?.let { if (it.toDouble() > bestTime) bestTime = it.toDouble() }
                    }
                }
            }
        }

        return if (targetDistance > 0) {
            val percentage = (bestDistance / targetDistance * 100).toInt()
            GoalProgress(bestDistance, targetDistance, percentage.coerceIn(0, 1000), "km")
        } else {
            val percentage = if (targetTime > 0) (bestTime / targetTime * 100).toInt() else 0
            GoalProgress(bestTime, targetTime, percentage.coerceIn(0, 1000), "min")
        }
    }

    private suspend fun calculateWeightProgress(goal: UserGoal.Weight): GoalProgress {
        val userData = onboardingRepository.getUserData().firstOrNull()
        val currentWeight = userData?.weight?.toDoubleOrNull() ?: 0.0
        val targetWeight = goal.targetWeight.toDoubleOrNull() ?: 0.0

        if (targetWeight <= 0 || currentWeight <= 0) return GoalProgress(currentWeight, targetWeight, 0)

        val diff = Math.abs(currentWeight - targetWeight)
        val percentage = if (currentWeight != 0.0) {
            (100 - (diff / currentWeight * 100)).toInt().coerceIn(0, 100)
        } else 0

        return GoalProgress(currentWeight, targetWeight, percentage, "kg")
    }
}
