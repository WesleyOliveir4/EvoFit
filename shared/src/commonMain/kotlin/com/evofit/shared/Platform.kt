package com.evofit.shared

interface Platform {
    val name: String
    val isAndroid: Boolean
    val isIos: Boolean
    val isDebug: Boolean
}

expect fun getPlatform(): Platform
