package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.WeightUpdate
import com.guaracode.evofit.domain.repository.AuthRepository
import com.guaracode.evofit.domain.repository.OnboardingRepository
import kotlinx.datetime.*

interface AddWeightUpdateUseCase {
    suspend operator fun invoke(weight: String)
}

class AddWeightUpdateUseCaseImpl(
    private val repository: OnboardingRepository,
    private val authRepository: AuthRepository
) : AddWeightUpdateUseCase {
    override suspend fun invoke(weight: String) {
        val userId = authRepository.getCurrentUserId() ?: return
        
        val currentInstant = Clock.System.now()
        val localDate = currentInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val day = localDate.dayOfMonth.toString().padStart(2, '0')
        val month = localDate.monthNumber.toString().padStart(2, '0')
        val year = localDate.year
        val currentDate = "$day/$month/$year"
        
        val weightUpdate = WeightUpdate(
            weight = weight,
            date = currentDate
        )
        
        repository.saveWeightUpdate(weightUpdate, userId)
    }
}
