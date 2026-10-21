package com.guaracode.evofit.core.monitoring

interface CrashReporter {
    fun logEvent(message: String)
    fun recordException(throwable: Throwable)
    fun setUserId(userId: String)
    fun setCustomKey(key: String, value: Any)
}

class NoOpCrashReporter : CrashReporter {
    override fun logEvent(message: String) {}
    override fun recordException(throwable: Throwable) {}
    override fun setUserId(userId: String) {}
    override fun setCustomKey(key: String, value: Any) {}
}
