package com.guaracode.evofit.presentation.ui.feature.profile.support.screen

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.guaracode.evofit.presentation.ui.feature.profile.support.components.SupportItem
import com.guaracode.evofit.presentation.ui.feature.profile.support.tracking.SupportTracker
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.new_workout_back_desc
import evofit.shared.generated.resources.support_description
import evofit.shared.generated.resources.support_option_email_desc
import evofit.shared.generated.resources.support_option_email_title
import evofit.shared.generated.resources.support_option_faq_desc
import evofit.shared.generated.resources.support_option_faq_title
import evofit.shared.generated.resources.support_option_workouts_desc
import evofit.shared.generated.resources.support_option_workouts_title
import evofit.shared.generated.resources.support_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun SupportScreen(
    onNavigate: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    tracker: SupportTracker = koinInject()
) {
    LaunchedEffect(Unit) {
        tracker.trackSupportScreenView()
    }
    SupportContent(
        onBackClick = onBackClick,
        onEmailClick = { onNavigate("support_email") },
        onSuggestWorkoutsClick = { onNavigate("suggestion_workouts") },
        onFaqClick = { onNavigate("faq") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportContent(
    onBackClick: () -> Unit,
    onEmailClick: () -> Unit,
    onSuggestWorkoutsClick: () -> Unit,
    onFaqClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.support_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.new_workout_back_desc),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
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
                    text = stringResource(Res.string.support_description),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = Dimens.SpacingSmall)
                )
            }

            item {
                SupportItem(
                    title = stringResource(Res.string.support_option_email_title),
                    description = stringResource(Res.string.support_option_email_desc),
                    icon = Icons.Default.Email,
                    onClick = onEmailClick
                )
            }

            item {
                SupportItem(
                    title = stringResource(Res.string.support_option_workouts_title),
                    description = stringResource(Res.string.support_option_workouts_desc),
                    icon = Icons.Default.Lightbulb,
                    onClick = onSuggestWorkoutsClick
                )
            }

            item {
                SupportItem(
                    title = stringResource(Res.string.support_option_faq_title),
                    description = stringResource(Res.string.support_option_faq_desc),
                    icon = Icons.AutoMirrored.Filled.HelpOutline,
                    onClick = onFaqClick
                )
            }
        }
    }
}

@Preview
@Composable
private fun SupportScreenPreview() {
    EvoFitTheme {
        SupportContent(
            onBackClick = {},
            onEmailClick = {},
            onSuggestWorkoutsClick = {},
            onFaqClick = {}
        )
    }
}
