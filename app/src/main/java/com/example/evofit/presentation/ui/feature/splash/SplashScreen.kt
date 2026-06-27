package com.example.evofit.presentation.ui.feature.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090909)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "EVOFIT",
            color = Color(0xFF5ED961),
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
