package com.example.evofit.presentation.ui.feature.commons.goals.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.model.*
import com.example.evofit.domain.usecase.GetExercisesByGroupUseCase
import com.example.evofit.domain.usecase.GetMuscleGroupsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

enum class GoalWizardStep {
    GOAL_TYPE,
    MUSCLE_GROUP,
    EXERCISE,
    GOAL_VALUE
}

data class GoalWizardUiState(
    val currentStep: GoalWizardStep = GoalWizardStep.GOAL_TYPE,
    val selectedCategory: String? = null,
    val selectedMuscle: MuscleGroup? = null,
    val selectedExercise: Exercise? = null,
    val goalValue: String = "",
    val timeValue: String = "",
    val search: String = "",
    val muscleGroups: List<MuscleGroup> = emptyList(),
    val filteredExercises: List<Exercise> = emptyList(),
    val confirmedGoal: UserGoal? = null
)

sealed class GoalAction {
    data class SelectCategory(val category: String, val isWeight: Boolean) : GoalAction()
    data class SelectMuscle(val muscle: MuscleGroup) : GoalAction()
    data class SelectExercise(val exercise: Exercise) : GoalAction()
    data class UpdateValue(val value: String) : GoalAction()
    data class UpdateTimeValue(val value: String) : GoalAction()
    data class UpdateSearch(val search: String) : GoalAction()
    data object Confirm : GoalAction()
    data object Back : GoalAction()
    data object Reset : GoalAction()
    data object ResetConfirmedGoal : GoalAction()
}

class GoalWizardViewModel(
    private val getMuscleGroupsUseCase: GetMuscleGroupsUseCase,
    private val getExercisesByGroupUseCase: GetExercisesByGroupUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GoalWizardUiState())
    val uiState: StateFlow<GoalWizardUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(muscleGroups = getMuscleGroupsUseCase()) }
    }

    fun initWithSuggestion(
        suggestion: GoalSuggestion?,
        strengthLabel: String,
        resistanceLabel: String,
        weightLossLabel: String,
        muscleGainLabel: String
    ) {
        if (suggestion == null) return

        val category = when {
            suggestion.isWeightGoal -> {
                if (suggestion.text.contains("perder", true) || suggestion.text.contains("peso", true)) weightLossLabel
                else muscleGainLabel
            }
            suggestion.category == ExerciseCategory.STRENGTH -> strengthLabel
            suggestion.category == ExerciseCategory.CARDIO -> resistanceLabel
            else -> null
        }

        val muscle = suggestion.muscleGroupId?.let { id ->
            _uiState.value.muscleGroups.find { it.id == id }
        }

        val exercises = muscle?.let { getExercisesByGroupUseCase(it.id) } ?: emptyList()
        val exercise = suggestion.exerciseId?.let { id ->
            exercises.find { it.id == id }
        }

        val step = when {
            suggestion.isWeightGoal -> GoalWizardStep.GOAL_VALUE
            exercise != null -> GoalWizardStep.GOAL_VALUE
            muscle != null -> GoalWizardStep.EXERCISE
            category != null -> GoalWizardStep.MUSCLE_GROUP
            else -> GoalWizardStep.GOAL_TYPE
        }

        _uiState.update {
            it.copy(
                currentStep = step,
                selectedCategory = category,
                selectedMuscle = muscle,
                selectedExercise = exercise,
                filteredExercises = exercises
            )
        }
    }

    fun onAction(action: GoalAction) {
        when (action) {
            is GoalAction.SelectCategory -> {
                _uiState.update {
                    it.copy(
                        selectedCategory = action.category,
                        currentStep = if (action.isWeight) GoalWizardStep.GOAL_VALUE else GoalWizardStep.MUSCLE_GROUP
                    )
                }
            }
            is GoalAction.SelectMuscle -> {
                val exercises = getExercisesByGroupUseCase(action.muscle.id)
                _uiState.update {
                    it.copy(
                        selectedMuscle = action.muscle,
                        selectedExercise = null,
                        filteredExercises = exercises,
                        currentStep = GoalWizardStep.EXERCISE,
                        search = ""
                    )
                }
            }
            is GoalAction.SelectExercise -> {
                _uiState.update {
                    it.copy(
                        selectedExercise = action.exercise,
                        currentStep = GoalWizardStep.GOAL_VALUE
                    )
                }
            }
            is GoalAction.UpdateValue -> _uiState.update { it.copy(goalValue = action.value) }
            is GoalAction.UpdateTimeValue -> _uiState.update { it.copy(timeValue = action.value) }
            is GoalAction.UpdateSearch -> _uiState.update { it.copy(search = action.search) }
            GoalAction.Back -> handleBack()
            GoalAction.Confirm -> handleConfirm()
            GoalAction.Reset -> _uiState.update {
                GoalWizardUiState(muscleGroups = getMuscleGroupsUseCase())
            }
            GoalAction.ResetConfirmedGoal -> _uiState.update { it.copy(confirmedGoal = null) }
        }
    }

    private fun handleBack() {
        _uiState.update { state ->
            when (state.currentStep) {
                GoalWizardStep.GOAL_TYPE -> state
                GoalWizardStep.MUSCLE_GROUP -> state.copy(currentStep = GoalWizardStep.GOAL_TYPE)
                GoalWizardStep.EXERCISE -> state.copy(currentStep = GoalWizardStep.MUSCLE_GROUP)
                GoalWizardStep.GOAL_VALUE -> {
                    // Check if it was weight category to skip muscle/exercise
                    val isWeight = state.selectedMuscle == null && state.selectedExercise == null
                    if (isWeight) state.copy(currentStep = GoalWizardStep.GOAL_TYPE)
                    else state.copy(currentStep = GoalWizardStep.EXERCISE)
                }
            }
        }
    }

    private fun handleConfirm() {
        val state = _uiState.value
        val isWeight = state.selectedMuscle == null && state.selectedExercise == null
        
        val goal = when {
            isWeight -> {
                UserGoal.Weight(UUID.randomUUID().toString(), state.goalValue)
            }
            state.selectedExercise != null -> {
                if (state.selectedMuscle?.category == ExerciseCategory.CARDIO) {
                    UserGoal.Cardio(
                        id = UUID.randomUUID().toString(),
                        type = state.selectedExercise.name,
                        distance = if (state.selectedExercise.unit == MeasurementUnit.DISTANCE) state.goalValue else null,
                        time = if (state.selectedExercise.unit == MeasurementUnit.DISTANCE) state.timeValue else state.goalValue
                    )
                } else {
                    UserGoal.Strength(
                        id = UUID.randomUUID().toString(),
                        exerciseName = state.selectedExercise.name,
                        value = state.goalValue,
                        unit = state.selectedExercise.unit
                    )
                }
            }
            else -> null
        }
        
        goal?.let { newGoal ->
            _uiState.update { it.copy(confirmedGoal = newGoal) }
        }
    }

    fun getFilteredMuscleGroups(strengthLabel: String): List<MuscleGroup> {
        val state = _uiState.value
        val isStrength = state.selectedCategory == strengthLabel
        return if (isStrength) {
            state.muscleGroups.filter { it.category == ExerciseCategory.STRENGTH }
        } else {
            state.muscleGroups.filter { it.category == ExerciseCategory.CARDIO }
        }
    }
}
