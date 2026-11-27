package com.evofit.shared

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
    override val isAndroid: Boolean = true
    override val isIos: Boolean = false
    override val isDebug: Boolean = com.evofit.shared.BuildConfig.DEBUG
}

actual fun getPlatform(): Platform = AndroidPlatform()
