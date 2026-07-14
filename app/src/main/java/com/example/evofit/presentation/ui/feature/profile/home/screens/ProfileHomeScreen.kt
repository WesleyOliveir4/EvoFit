package com.example.evofit.presentation.ui.feature.profile.home.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.profile.home.components.ProfileMenuItem
import com.example.evofit.presentation.ui.theme.AppDarkBg
import com.example.evofit.presentation.ui.theme.AppSurface
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import com.example.evofit.presentation.ui.theme.TextPrimary
import com.example.evofit.presentation.ui.theme.TextSecondary

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.evofit.presentation.ui.feature.profile.home.viewmodel.ProfileViewModel
import com.example.evofit.navigation.NavRoutes
import com.example.evofit.presentation.ui.feature.components.AppBottomNavigation
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileHomeScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    onNavigate: (String) -> Unit = {},
    onUserDataClick: () -> Unit = {},
    onGoalsClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    ProfileHomeScreenContent(
        userName = uiState.name,
        userAge = uiState.age,
        userWeight = uiState.weight,
        totalWorkouts = uiState.totalWorkouts,
        records = uiState.records,
        goals = uiState.goals,
        onNavigate = onNavigate,
        onUserDataClick = onUserDataClick,
        onGoalsClick = onGoalsClick
    )
}

@Composable
fun ProfileHomeScreenContent(
    modifier: Modifier = Modifier,
    userName: String,
    userAge: String,
    userWeight: String,
    totalWorkouts: String = "0",
    records: String = "0",
    goals: String = "0",
    onNavigate: (String) -> Unit,
    onUserDataClick: () -> Unit,
    onGoalsClick: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        containerColor = AppDarkBg,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.nav_profile),
                    color = TextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black
                )
            }
        },
        bottomBar = {
            AppBottomNavigation(
                currentRoute = NavRoutes.Profile.route,
                onNavigate = onNavigate
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = AppSurface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = userName,
                        color = TextPrimary,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(id = R.string.profile_user_summary_format, userAge, userWeight),
                        color = TextSecondary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = stringResource(id = R.string.profile_my_account),
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                ProfileMenuItem(
                    title = stringResource(id = R.string.profile_user_data),
                    icon = Icons.Default.Person,
                    onClick = onUserDataClick
                )

                ProfileMenuItem(
                    title = stringResource(id = R.string.profile_personal_goals),
                    icon = Icons.Default.Star,
                    onClick = onGoalsClick
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = stringResource(id = R.string.profile_statistics),
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                com.example.evofit.presentation.ui.feature.profile.home.components.ProfileStatsCard(
                    totalWorkouts = totalWorkouts,
                    records = records,
                    goals = goals
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileHomeScreenPreview() {
    EvoFitTheme {
        ProfileHomeScreenContent(
            userName = "Wesley",
            userAge = "28",
            userWeight = "78",
            onNavigate = {},
            onUserDataClick = {},
            onGoalsClick = {}
        )
    }
}
