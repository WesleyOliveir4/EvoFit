package com.example.evofit.presentation.ui.feature.workout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.repository.OnboardingRepository
import com.example.evofit.domain.usecase.SaveWorkoutUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.Date

class NewWorkoutViewModel(
    private val onboardingRepository: OnboardingRepository,
    private val saveWorkoutUseCase: SaveWorkoutUseCase
) : ViewModel() {

    private val _workoutSaved = MutableSharedFlow<Unit>()
    val workoutSaved = _workoutSaved.asSharedFlow()

    fun selectMuscleGroup(groupId: String) {
        viewModelScope.launch {
            val userId = onboardingRepository.getUserId() ?: return@launch
            
            val newWorkout = Workout(
                userId = userId,
                muscleGroupId = groupId.replaceFirstChar { it.uppercase() },
                date = System.currentTimeMillis(),
                isCompleted = false,
                exercises = emptyList() // Por enquanto sem exercícios, apenas cria o treino
            )
            
            saveWorkoutUseCase(newWorkout)
            _workoutSaved.emit(Unit)
        }
    }
}
