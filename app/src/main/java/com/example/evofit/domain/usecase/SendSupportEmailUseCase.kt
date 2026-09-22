package com.example.evofit.domain.usecase

interface SendSupportEmailUseCase {
    suspend operator fun invoke(topic: String, message: String): Result<Unit>
}
