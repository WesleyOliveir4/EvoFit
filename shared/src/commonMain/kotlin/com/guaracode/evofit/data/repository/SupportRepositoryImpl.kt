package com.guaracode.evofit.data.repository

import com.guaracode.evofit.core.monitoring.CrashReporter
import com.guaracode.evofit.domain.repository.SupportRepository

expect class SupportRepositoryImpl : SupportRepository {
    override suspend fun sendSupportEmail(topic: String, message: String): Result<Unit>
}
