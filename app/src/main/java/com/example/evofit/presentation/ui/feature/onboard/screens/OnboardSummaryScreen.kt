package com.example.evofit.presentation.ui.feature.onboard.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.presentation.ui.feature.onboard.components.OnboardingButton
import com.example.evofit.presentation.ui.feature.onboard.components.PageIndicators
import com.example.evofit.presentation.ui.feature.onboard.viewmodel.OnboardingViewModel
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardSummaryScreen(
    currentPage: Int,
    totalPages: Int,
    onStartTraining: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val userData by viewModel.userData.collectAsStateWithLifecycle()

    OnboardSummaryContent(
        userData = userData,
        currentPage = currentPage,
        totalPages = totalPages,
        onStartTraining = remember { 
            { 
                viewModel.finishOnboarding(onStartTraining) 
            }
        }
    )
}

@Composable
fun OnboardSummaryContent(
    userData: UserOnboardingData,
    currentPage: Int,
    totalPages: Int,
    onStartTraining: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.onboarding_summary_title),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.onboarding_summary_description),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.onboarding_summary_label_summary),
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                SummaryRow(
                    icon = Icons.Default.AccountCircle,
                    label = stringResource(R.string.onboarding_summary_label_name),
                    value = userData.name
                )

                SummaryRow(
                    icon = Icons.Default.DateRange,
                    label = stringResource(R.string.onboarding_summary_label_age),
                    value = stringResource(R.string.onboarding_summary_value_age, userData.age)
                )

                SummaryRow(
                    icon = Icons.Default.Favorite,
                    label = stringResource(R.string.onboarding_summary_label_weight),
                    value = stringResource(R.string.onboarding_summary_value_weight, userData.weight)
                )

                SummaryRow(
                    icon = Icons.Default.Straighten,
                    label = stringResource(R.string.onboarding_summary_label_height),
                    value = stringResource(R.string.onboarding_summary_value_height, userData.height)
                )

                if (userData.goals.isNotEmpty()) {
                    SummaryRow(
                        icon = Icons.Default.Star,
                        label = stringResource(R.string.onboarding_summary_label_goals),
                        value = "${userData.goals.size}"
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            PageIndicators(
                pageCount = totalPages,
                selectedPage = currentPage,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OnboardingButton(
                text = stringResource(R.string.onboarding_summary_button_start_training),
                onClick = onStartTraining
            )

        }
    }
}

@Composable
fun SummaryRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }

        Column {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 14.sp
            )
            Text(
                text = value,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
fun OnboardSummaryScreenPreview() {
    EvoFitTheme {
        OnboardSummaryContent(
            userData = UserOnboardingData(
                name = "Wesley",
                age = "28",
                weight = "78",
                height = "175",
                goals = emptyList()
            ),
            currentPage = 3,
            totalPages = 4,
            onStartTraining = {}
        )
    }
}
