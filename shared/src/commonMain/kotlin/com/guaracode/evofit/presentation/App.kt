package com.guaracode.evofit.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.guaracode.evofit.navigation.AuthNavigation
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme

@Composable
fun App() {
    EvoFitTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AuthNavigation()
        }
    }
}
