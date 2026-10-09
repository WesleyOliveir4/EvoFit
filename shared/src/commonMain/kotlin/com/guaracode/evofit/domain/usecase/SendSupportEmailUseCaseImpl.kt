package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.repository.SupportRepository

class SendSupportEmailUseCaseImpl(
    private val repository: SupportRepository
) : SendSupportEmailUseCase {
    override suspend fun invoke(topic: String, message: String): Result<Unit> {
        val taggedTopic = "EvoFit - $topic"
        return repository.sendSupportEmail(taggedTopic, message)
    }
}
