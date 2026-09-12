package com.example.evofit.presentation.ui.feature.profile.goals.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.model.UserGoal
import com.example.evofit.domain.usecase.GetExercisesByGroupUseCase
import com.example.evofit.domain.usecase.GetMuscleGroupsUseCase
import com.example.evofit.domain.usecase.GetUserIdUseCase
import com.example.evofit.domain.usecase.profile.CalculateGoalProgressUseCase
import com.example.evofit.domain.usecase.profile.GetActiveUserGoalsUseCase
import com.example.evofit.presentation.mapper.toImageRes
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PersonalGoalsUiState(
    val goals: List<GoalUiModel> = emptyList(),
    val isLoading: Boolean = false
)

data class GoalUiModel(
    val id: String,
    val title: String,
    val category: String,
    val currentValue: String,
    val targetValue: String,
    val percentage: Int,
    val iconRes: Int? = null
)

class PersonalGoalsViewModel(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getActiveUserGoalsUseCase: GetActiveUserGoalsUseCase,
    private val calculateGoalProgressUseCase: CalculateGoalProgressUseCase,
    private val onboardingRepository: com.example.evofit.domain.repository.OnboardingRepository,
    private val getMuscleGroupsUseCase: GetMuscleGroupsUseCase,
    private val getExercisesByGroupUseCase: GetExercisesByGroupUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PersonalGoalsUiState())
    val uiState: StateFlow<PersonalGoalsUiState> = _uiState.asStateFlow()

    init {
        loadGoals()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadGoals() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Pre-load muscle group mapping for exercises
            val exerciseToMuscleGroup = mutableMapOf<String, com.example.evofit.domain.model.MuscleGroupType>()
            getMuscleGroupsUseCase().forEach { group ->
                getExercisesByGroupUseCase(group.id).forEach { exercise ->
                    exerciseToMuscleGroup[exercise.name] = group.type
                }
            }

            getUserIdUseCase().flatMapLatest { userId ->
                if (userId == null) {
                    flowOf(emptyList<GoalUiModel>())
                } else {
                    getActiveUserGoalsUseCase()
                        .flatMapLatest { goals ->
                            if (goals.isEmpty()) {
                                flowOf(emptyList<GoalUiModel>())
                            } else {
                                val goalFlows = goals.map { goal ->
                                    calculateGoalProgressUseCase(goal, userId).map { progress ->
                                        val title = when (goal) {
                                            is UserGoal.Strength -> goal.exerciseName
                                            is UserGoal.Cardio -> goal.type
                                            is UserGoal.Weight -> "Peso Corporal"
                                        }

                                        val category = when (goal) {
                                            is UserGoal.Strength -> "Força"
                                            is UserGoal.Cardio -> "Cardio"
                                            is UserGoal.Weight -> "Peso"
                                        }

                                        val iconRes = when (goal) {
                                            is UserGoal.Strength -> {
                                                val muscleType = exerciseToMuscleGroup[goal.exerciseName]
                                                (muscleType ?: com.example.evofit.domain.model.MuscleGroupType.OTHER).toImageRes()
                                            }
                                            is UserGoal.Cardio -> com.example.evofit.domain.model.MuscleGroupType.CARDIO.toImageRes()
                                            is UserGoal.Weight -> com.example.evofit.R.drawable.ic_balance_2
                                        }

                                        val curValue = if (goal is UserGoal.Cardio && progress.currentTime != null && progress.unit == "km") {
                                            "%.1f em %.0fmin".format(progress.currentValue, progress.currentTime)
                                        } else if (progress.unit == "min") {
                                            "%.0fmin".format(progress.currentValue)
                                        } else {
                                            "%.1f${progress.unit}".format(progress.currentValue)
                                        }

                                        val tarValue = if (goal is UserGoal.Cardio && progress.targetTime != null && progress.unit == "km") {
                                            "%.1fkm em %.0fmin".format(progress.targetValue, progress.targetTime)
                                        } else if (progress.unit == "min") {
                                            "%.0fmin".format(progress.targetValue)
                                        } else {
                                            "%.1f${progress.unit}".format(progress.targetValue)
                                        }

                                        GoalUiModel(
                                            id = goal.id,
                                            title = title,
                                            category = category,
                                            currentValue = curValue,
                                            targetValue = tarValue,
                                            percentage = progress.percentage,
                                            iconRes = iconRes
                                        )
                                    }
                                }
                                combine(goalFlows) { it.toList() }
                            }
                        }
                }
            }.collect { uiGoals ->
                _uiState.update { it.copy(goals = uiGoals, isLoading = false) }
            }
        }
    }

    fun addGoal(goal: UserGoal) {
        viewModelScope.launch {
            val userId = getUserIdUseCase().firstOrNull() ?: return@launch
            val currentData = onboardingRepository.getUserData().firstOrNull()
            val updatedData = currentData?.copy(
                goals = currentData.goals + goal
            ) ?: com.example.evofit.domain.model.UserOnboardingData(goals = listOf(goal))
            
            onboardingRepository.saveUserData(updatedData, userId, true)
        }
    }

    fun deleteGoal(goalId: String) {
        viewModelScope.launch {
            onboardingRepository.deleteGoal(goalId)
        }
    }
}
