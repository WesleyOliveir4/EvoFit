package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.WorkoutRepository
import com.example.evofit.presentation.mapper.DateMapper
import java.util.Calendar
import java.util.Date

class GetWorkoutsCountUseCase(
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(userId: String, period: String): Int {
        val history = repository.getWorkoutDoneHistory(userId)
        val startDate = getStartDateForPeriod(period)
        
        return if (startDate == null) {
            history.size
        } else {
            history.count { workout ->
                val workoutDate = DateMapper.parseDate(workout.date)
                workoutDate != null && workoutDate.after(startDate)
            }
        }
    }

    private fun getStartDateForPeriod(period: String): Date? {
        val calendar = Calendar.getInstance()
        return when (period) {
            "1 mês" -> {
                calendar.add(Calendar.MONTH, -1)
                calendar.time
            }
            "3 meses" -> {
                calendar.add(Calendar.MONTH, -3)
                calendar.time
            }
            "6 meses" -> {
                calendar.add(Calendar.MONTH, -6)
                calendar.time
            }
            else -> null // "Tudo"
        }
    }
}
