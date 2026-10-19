package com.guaracode.evofit.presentation.ui.feature.profile.goals.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guaracode.evofit.domain.model.MuscleGroupType
import com.guaracode.evofit.domain.model.UserGoal
import com.guaracode.evofit.domain.model.UserOnboardingData
import com.guaracode.evofit.domain.repository.OnboardingRepository
import com.guaracode.evofit.domain.usecase.GetExercisesByGroupUseCase
import com.guaracode.evofit.domain.usecase.GetMuscleGroupsUseCase
import com.guaracode.evofit.domain.usecase.GetUserIdUseCase
import com.guaracode.evofit.domain.usecase.profile.CalculateGoalProgressUseCase
import com.guaracode.evofit.domain.usecase.profile.GetActiveUserGoalsUseCase
import com.guaracode.evofit.presentation.mapper.toImageRes
import com.guaracode.evofit.presentation.ui.feature.profile.goals.tracking.PersonalGoalsTracker
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.ic_balance_2
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
import org.jetbrains.compose.resources.DrawableResource

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
    val iconRes: DrawableResource? = null
)

private fun Double.formatVal(): String {
    val rounded = (this * 10).toInt()
    val integerPart = rounded / 10
    val decimalPart = kotlin.math.abs(rounded % 10)
    return "$integerPart,$decimalPart"
}

class PersonalGoalsViewModel(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getActiveUserGoalsUseCase: GetActiveUserGoalsUseCase,
    private val calculateGoalProgressUseCase: CalculateGoalProgressUseCase,
    private val onboardingRepository: OnboardingRepository,
    private val getMuscleGroupsUseCase: GetMuscleGroupsUseCase,
    private val getExercisesByGroupUseCase: GetExercisesByGroupUseCase,
    private val tracker: PersonalGoalsTracker
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

            val exerciseToMuscleGroup = mutableMapOf<String, MuscleGroupType>()
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
                                                (muscleType ?: MuscleGroupType.OTHER).toImageRes()
                                            }
                                            is UserGoal.Cardio -> MuscleGroupType.CARDIO.toImageRes()
                                            is UserGoal.Weight -> Res.drawable.ic_balance_2
                                        }

                                        val curValue = if (goal is UserGoal.Cardio && progress.currentTime != null && progress.unit == "km") {
                                            "${progress.currentValue.formatVal()} em ${progress.currentTime.toInt()}min"
                                        } else if (progress.unit == "min") {
                                            "${progress.currentValue.toInt()}min"
                                        } else {
                                            "${progress.currentValue.formatVal()}${progress.unit}"
                                        }

                                        val tarValue = if (goal is UserGoal.Cardio && progress.targetTime != null && progress.unit == "km") {
                                            "${progress.targetValue.formatVal()}km em ${progress.targetTime.toInt()}min"
                                        } else if (progress.unit == "min") {
                                            "${progress.targetValue.toInt()}min"
                                        } else {
                                            "${progress.targetValue.formatVal()}${progress.unit}"
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
        tracker.trackGoalAdded(goal::class.simpleName ?: "UserGoal")
        viewModelScope.launch {
            val userId = getUserIdUseCase().firstOrNull() ?: return@launch
            val currentData = onboardingRepository.getUserData().firstOrNull()
            val updatedData = currentData?.copy(
                goals = currentData.goals + goal
            ) ?: UserOnboardingData(goals = listOf(goal))
            
            onboardingRepository.saveUserData(updatedData, userId, true)
        }
    }

    fun deleteGoal(goalId: String) {
        tracker.trackGoalDeleted(goalId)
        viewModelScope.launch {
            onboardingRepository.deleteGoal(goalId)
        }
    }
}
