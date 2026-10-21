package com.guaracode.evofit.presentation

import androidx.compose.ui.window.ComposeUIViewController
import com.guaracode.evofit.di.sharedAppModules
import com.guaracode.evofit.navigation.NavNavigation
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initKoin()
    return ComposeUIViewController {
        EvoFitTheme {
            NavNavigation()
        }
    }
}

private var isKoinInitialized = false

private fun initKoin() {
    if (!isKoinInitialized) {
        startKoin {
            modules(sharedAppModules)
        }
        isKoinInitialized = true
    }
}
