package com.example.evofit.presentation.ui.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.evofit.R
import com.example.evofit.presentation.ui.theme.AppDarkBg
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    onNavigate: (String) -> Unit,
    viewModel: SplashViewModel = koinViewModel()
) {
    val startDestination by viewModel.startDestination.collectAsState()

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
            .background(AppDarkBg),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_splash_logo),
            contentDescription = "Logo EvoFit",
            modifier = Modifier.size(318.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    SplashContent()
}
