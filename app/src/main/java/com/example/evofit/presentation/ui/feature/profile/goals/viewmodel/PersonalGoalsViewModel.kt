package com.example.evofit.presentation.ui.feature.profile.goals.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.model.Exercise
import com.example.evofit.domain.model.MuscleGroup
import com.example.evofit.domain.model.UserGoal
import com.example.evofit.domain.usecase.GetUserIdUseCase
import com.example.evofit.domain.usecase.profile.CalculateGoalProgressUseCase
import com.example.evofit.domain.usecase.profile.GetActiveUserGoalsUseCase
import com.example.evofit.domain.usecase.GetExerciseDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
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
    val originalGoal: UserGoal
)

class PersonalGoalsViewModel(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getActiveUserGoalsUseCase: GetActiveUserGoalsUseCase,
    private val calculateGoalProgressUseCase: CalculateGoalProgressUseCase,
    private val onboardingRepository: com.example.evofit.domain.repository.OnboardingRepository,
    private val getExerciseDataUseCase: GetExerciseDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PersonalGoalsUiState())
    val uiState: StateFlow<PersonalGoalsUiState> = _uiState.asStateFlow()

    init {
        loadGoals()
    }

    private fun loadGoals() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val userId = getUserIdUseCase() ?: return@launch
            
            getActiveUserGoalsUseCase().collect { goals ->
                val uiGoals = goals.map { goal ->
                    val progress = calculateGoalProgressUseCase(goal, userId)
                    
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

                    val unit = progress.unit
                    
                    GoalUiModel(
                        id = goal.id,
                        title = title,
                        category = category,
                        currentValue = "${progress.currentValue}$unit",
                        targetValue = "${progress.targetValue}$unit",
                        percentage = progress.percentage,
                        originalGoal = goal
                    )
                }
                _uiState.update { it.copy(goals = uiGoals, isLoading = false) }
            }
        }
    }

    fun addGoal(goal: UserGoal) {
        viewModelScope.launch {
            val userId = getUserIdUseCase() ?: return@launch
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

    fun getMuscleGroups(): List<MuscleGroup> = getExerciseDataUseCase.getMuscleGroups()
    fun getExercisesByGroup(groupId: String): List<Exercise> = getExerciseDataUseCase.getExercisesByGroup(groupId)
}
