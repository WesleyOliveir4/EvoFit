package com.example.evofit.domain.repository

interface SupportRepository {
    suspend fun sendSupportEmail(topic: String, message: String): Result<Unit>
}
