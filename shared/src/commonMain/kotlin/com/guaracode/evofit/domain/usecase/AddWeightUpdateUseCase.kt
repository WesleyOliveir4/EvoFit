package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.WeightUpdate
import com.guaracode.evofit.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.*

interface AddWeightUpdateUseCase {
    suspend operator fun invoke(weight: String): Result<Unit>
}

class AddWeightUpdateUseCaseImpl(
    private val onboardingRepository: OnboardingRepository,
    private val getUserIdUseCase: GetUserIdUseCase
) : AddWeightUpdateUseCase {
    override suspend fun invoke(weight: String): Result<Unit> {
        return try {
            val userId = getUserIdUseCase().firstOrNull() ?: ""
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            val day = now.dayOfMonth.toString().padStart(2, '0')
            val month = now.monthNumber.toString().padStart(2, '0')
            val year = now.year
            val dateStr = "$day/$month/$year"

            val weightUpdate = WeightUpdate(
                id = randomId(),
                weight = weight,
                date = dateStr
            )
            onboardingRepository.saveWeightUpdate(weightUpdate, userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun randomId(): String {
        return kotlin.random.Random.nextBits(32).toString()
    }
}
