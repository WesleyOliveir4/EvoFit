package com.evofit.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
