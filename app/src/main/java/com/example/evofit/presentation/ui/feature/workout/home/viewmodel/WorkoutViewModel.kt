package com.example.evofit.presentation.ui.feature.workout.home.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.usecase.GetOnboardingDataUseCase
import com.example.evofit.domain.usecase.GetUserIdUseCase
import com.example.evofit.domain.usecase.GetWorkoutsUseCase
import com.example.evofit.domain.usecase.UpdateWorkoutsOrderUseCase
import com.example.evofit.presentation.model.WorkoutUIModel
import com.example.evofit.presentation.ui.feature.workout.home.state.WorkoutState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WorkoutViewModel(
    private val getOnboardingDataUseCase: GetOnboardingDataUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getWorkoutsUseCase: GetWorkoutsUseCase,
    private val updateWorkoutsOrderUseCase: UpdateWorkoutsOrderUseCase
) : ViewModel() {

    private var updateOrderJob: Job? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<WorkoutState> = getOnboardingDataUseCase()
        .flatMapLatest { userData ->
            val userId = getUserIdUseCase() ?: ""
            if (userId.isEmpty()) {
                flowOf(WorkoutState(userName = userData.name))
            } else {
                getWorkoutsUseCase(userId).map { workouts ->
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    val startOfWeek = calendar.timeInMillis
                    
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("pt-BR"))

                    WorkoutState(
                        userName = userData.name,
                        workouts = workouts.map { workout ->
                            WorkoutUIModel(
                                id = workout.id.toInt(),
                                title = workout.name.ifEmpty { workout.muscleGroupId },
                                exercises = workout.exercises.size,
                                series = workout.exercises.sumOf { it.sets.size },
                                icon = Icons.Default.FitnessCenter
                            )
                        },
                        totalWorkouts = workouts.size,
                        workoutsThisWeek = workouts.count { 
                            try {
                                val date = dateFormat.parse(it.date)
                                date != null && date.time >= startOfWeek
                            } catch (e: Exception) {
                                false
                            }
                        }
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = WorkoutState()
        )

    fun updateWorkoutOrder(orderedList: List<WorkoutUIModel>) {
        updateOrderJob?.cancel()
        updateOrderJob = viewModelScope.launch {
            delay(1000) // Debounce de 1 segundo
            
            val userId = getUserIdUseCase() ?: return@launch
            // Buscamos os workouts atuais para manter os dados completos ao salvar a nova ordem
            val currentWorkouts = getWorkoutsUseCase(userId).first()
            
            val reorderedWorkouts = orderedList.mapNotNull { uiModel ->
                currentWorkouts.find { it.id.toInt() == uiModel.id }
            }
            
            updateWorkoutsOrderUseCase(reorderedWorkouts)
        }
    }
}