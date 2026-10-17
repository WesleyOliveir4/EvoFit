package com.guaracode.evofit.data.repository

import com.guaracode.evofit.core.monitoring.CrashReporter
import com.guaracode.evofit.domain.repository.SupportRepository
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual class SupportRepositoryImpl(
    private val crashReporter: CrashReporter
) : SupportRepository {
    actual override suspend fun sendSupportEmail(topic: String, message: String): Result<Unit> {
        return try {
            val url = NSURL(string = "mailto:guaracode@gmail.com?subject=$topic&body=$message")
            if (UIApplication.sharedApplication.canOpenURL(url)) {
                UIApplication.sharedApplication.openURL(url)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Cannot open mail client"))
            }
        } catch (e: Exception) {
            crashReporter.recordException(e)
            Result.failure(e)
        }
    }
}
