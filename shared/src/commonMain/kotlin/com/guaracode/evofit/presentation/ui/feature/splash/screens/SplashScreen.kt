package com.guaracode.evofit.presentation.ui.feature.splash.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.guaracode.evofit.presentation.ui.feature.splash.tracking.SplashTracker
import com.guaracode.evofit.presentation.ui.feature.splash.viewmodel.SplashViewModel
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.ic_splash_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreen(
    onNavigate: (String) -> Unit,
    viewModel: SplashViewModel = koinViewModel(),
    tracker: SplashTracker = koinInject()
) {
    val startDestination by viewModel.startDestination.collectAsState()

    LaunchedEffect(Unit) {
        tracker.trackSplashScreenView()
    }

    LaunchedEffect(startDestination) {
        startDestination?.let { destination ->
            onNavigate(destination)
        }
    }

    SplashContent()
}

@Composable
fun SplashContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_splash_logo),
            contentDescription = "Logo EvoFit",
            modifier = Modifier.size(318.dp)
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    EvoFitTheme {
        SplashContent()
    }
}
