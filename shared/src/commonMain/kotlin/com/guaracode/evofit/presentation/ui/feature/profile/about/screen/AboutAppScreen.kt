package com.guaracode.evofit.presentation.ui.feature.profile.about.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.GppGood
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.guaracode.evofit.presentation.ui.feature.profile.about.state.AboutAppUiState
import com.guaracode.evofit.presentation.ui.feature.profile.about.viewmodel.AboutAppViewModel
import com.guaracode.evofit.presentation.ui.feature.profile.support.components.SupportItem
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.about_app_name
import evofit.shared.generated.resources.about_description
import evofit.shared.generated.resources.about_developer_name
import evofit.shared.generated.resources.about_option_developer
import evofit.shared.generated.resources.about_option_privacy
import evofit.shared.generated.resources.about_option_terms
import evofit.shared.generated.resources.about_option_version
import evofit.shared.generated.resources.about_slogan
import evofit.shared.generated.resources.about_title
import evofit.shared.generated.resources.ic_logo_evofit
import evofit.shared.generated.resources.new_workout_back_desc
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AboutAppScreen(
    onBackClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
    viewModel: AboutAppViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.trackScreenView()
    }

    AboutAppContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onTermsClick = onTermsClick,
        onPrivacyClick = onPrivacyClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutAppContent(
    uiState: AboutAppUiState,
    onBackClick: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.about_title),
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge),
            contentPadding = PaddingValues(top = Dimens.SpacingMedium, bottom = Dimens.SpacingLarge)
        ) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_logo_evofit),
                        contentDescription = null,
                        modifier = Modifier.size(Dimens.OnboardingLogoSize)
                    )

                    Text(
                        text = stringResource(Res.string.about_app_name),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Text(
                        text = stringResource(Res.string.about_slogan),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(Dimens.SpacingSmall))

                    Text(
                        text = stringResource(Res.string.about_description),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = Dimens.SpacingMedium)
                    )
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)) {
                    SupportItem(
                        title = stringResource(Res.string.about_option_version),
                        description = uiState.appVersion,
                        icon = Icons.AutoMirrored.Filled.FactCheck,
                        onClick = { }
                    )

                    SupportItem(
                        title = stringResource(Res.string.about_option_developer),
                        description = stringResource(Res.string.about_developer_name),
                        icon = Icons.Default.Business,
                        onClick = { }
                    )

                    SupportItem(
                        title = stringResource(Res.string.about_option_terms),
                        description = "",
                        icon = Icons.AutoMirrored.Filled.Assignment,
                        onClick = onTermsClick
                    )

                    SupportItem(
                        title = stringResource(Res.string.about_option_privacy),
                        description = "",
                        icon = Icons.Default.GppGood,
                        onClick = onPrivacyClick
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AboutAppScreenPreview() {
    EvoFitTheme {
        AboutAppContent(
            uiState = AboutAppUiState(appVersion = "1.0.1"),
            onBackClick = {},
            onTermsClick = {},
            onPrivacyClick = {}
        )
    }
}
