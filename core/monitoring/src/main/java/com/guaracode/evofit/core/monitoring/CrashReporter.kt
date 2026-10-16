package com.guaracode.evofit.core.monitoring

interface CrashReporter {
    fun logEvent(message: String)
    fun recordException(throwable: Throwable)
    fun setUserId(userId: String)
    fun setCustomKey(key: String, value: Any)
}
