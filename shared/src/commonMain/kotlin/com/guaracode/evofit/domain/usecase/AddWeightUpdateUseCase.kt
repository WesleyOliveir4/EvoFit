package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.WeightUpdate
import com.guaracode.evofit.domain.repository.AuthRepository
import com.guaracode.evofit.domain.repository.OnboardingRepository
import java.text.SimpleDateFormat
import java.util.*

interface AddWeightUpdateUseCase {
    suspend operator fun invoke(weight: String)
}

class AddWeightUpdateUseCaseImpl(
    private val repository: OnboardingRepository,
    private val authRepository: AuthRepository
) : AddWeightUpdateUseCase {
    override suspend fun invoke(weight: String) {
        val userId = authRepository.getCurrentUserId() ?: return
        
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
        val currentDate = sdf.format(Date())
        
        val weightUpdate = WeightUpdate(
            weight = weight,
            date = currentDate
        )
        
        repository.saveWeightUpdate(weightUpdate, userId)
    }
}
