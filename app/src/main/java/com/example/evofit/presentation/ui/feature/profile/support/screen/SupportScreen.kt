package com.example.evofit.presentation.ui.feature.profile.support.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.profile.support.components.SupportItem
import com.example.evofit.presentation.ui.theme.AppDarkBg
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import com.example.evofit.presentation.ui.theme.TextPrimary
import com.example.evofit.presentation.ui.theme.TextSecondary

@Composable
fun SupportScreen(
    onBackClick: () -> Unit = {}
) {
    SupportContent(
        onBackClick = onBackClick,
        onEmailClick = { /* TODO: Open Email */ },
        onSuggestWorkoutsClick = { /* TODO: Suggest */ },
        onFaqClick = { /* TODO: FAQ */ },
        onTutorialsClick = { /* TODO: Tutorials */ }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportContent(
    onBackClick: () -> Unit,
    onEmailClick: () -> Unit,
    onSuggestWorkoutsClick: () -> Unit,
    onFaqClick: () -> Unit,
    onTutorialsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppDarkBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.support_title),
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.new_workout_back_desc),
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppDarkBg)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Dimens.ScreenPaddingHorizontal),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium),
            contentPadding = PaddingValues(top = Dimens.SpacingMedium, bottom = Dimens.SpacingLarge)
        ) {
            item {
                Text(
                    text = stringResource(id = R.string.support_description),
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = Dimens.SpacingSmall)
                )
            }

            item {
                SupportItem(
                    title = stringResource(id = R.string.support_option_email_title),
                    description = stringResource(id = R.string.support_option_email_desc),
                    icon = Icons.Default.Email,
                    onClick = onEmailClick
                )
            }

            item {
                SupportItem(
                    title = stringResource(id = R.string.support_option_workouts_title),
                    description = stringResource(id = R.string.support_option_workouts_desc),
                    icon = Icons.Default.Lightbulb,
                    onClick = onSuggestWorkoutsClick
                )
            }

            item {
                SupportItem(
                    title = stringResource(id = R.string.support_option_faq_title),
                    description = stringResource(id = R.string.support_option_faq_desc),
                    icon = Icons.AutoMirrored.Filled.HelpOutline,
                    onClick = onFaqClick
                )
            }

            item {
                SupportItem(
                    title = stringResource(id = R.string.support_option_tutorials_title),
                    description = stringResource(id = R.string.support_option_tutorials_desc),
                    icon = Icons.Default.PlayCircle,
                    onClick = onTutorialsClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SupportScreenPreview() {
    EvoFitTheme {
        SupportContent(
            onBackClick = {},
            onEmailClick = {},
            onSuggestWorkoutsClick = {},
            onFaqClick = {},
            onTutorialsClick = {}
        )
    }
}
