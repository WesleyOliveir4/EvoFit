package com.example.evofit.presentation.ui.feature.home.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.evofit.domain.model.UserGoal
import com.example.evofit.presentation.mapper.getDisplayText
import com.example.evofit.presentation.ui.feature.home.viewmodel.HomeViewModel
import androidx.compose.ui.platform.LocalContext
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val userData by viewModel.userData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090909))
            .systemBarsPadding()
            .padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Olá, ${userData.name}! 👋",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Sua jornada de evolução continua.",
            color = Color(0xFFB0BEC5),
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (userData.goals.isNotEmpty()) {
            Text(
                text = "Suas Metas",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            val context = LocalContext.current
            userData.goals.forEach { goal ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .background(Color(0xFF1E1E1E), androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = goal.getDisplayText(context),
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        val subtitle = when(goal) {
                            is UserGoal.Strength -> "Força"
                            is UserGoal.Cardio -> "Cardio"
                            is UserGoal.Weight -> "Meta de Peso"
                        }
                        Text(
                            text = subtitle,
                            color = Color(0xFF67D14E),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
