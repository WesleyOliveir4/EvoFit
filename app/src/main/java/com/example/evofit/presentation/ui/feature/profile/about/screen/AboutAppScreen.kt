package com.example.evofit.presentation.ui.feature.profile.about.screen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.profile.support.components.SupportItem
import com.example.evofit.presentation.ui.theme.AppDarkBg
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import com.example.evofit.presentation.ui.theme.TextPrimary
import com.example.evofit.presentation.ui.theme.TextSecondary

@Composable
fun AboutAppScreen(
    onBackClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {}
) {
    AboutAppContent(
        onBackClick = onBackClick,
        onTermsClick = onTermsClick,
        onPrivacyClick = onPrivacyClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutAppContent(
    onBackClick: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppDarkBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.about_title),
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
                        painter = painterResource(id = R.drawable.ic_logo_evofit),
                        contentDescription = null,
                        modifier = Modifier.size(Dimens.OnboardingLogoSize)
                    )
                    
                    Text(
                        text = stringResource(id = R.string.about_app_name),
                        color = TextPrimary,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    
                    Text(
                        text = stringResource(id = R.string.about_slogan),
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    
                    Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                    
                    Text(
                        text = stringResource(id = R.string.about_description),
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = Dimens.SpacingMedium)
                    )
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)) {
                    SupportItem(
                        title = stringResource(id = R.string.about_option_version),
                        description = stringResource(id = R.string.about_app_version),
                        icon = Icons.AutoMirrored.Filled.FactCheck,
                        onClick = { /* Nothing for now */ }
                    )
                    
                    SupportItem(
                        title = stringResource(id = R.string.about_option_developer),
                        description = stringResource(id = R.string.about_developer_name),
                        icon = Icons.Default.Business,
                        onClick = { /* Nothing for now */ }
                    )
                    
                    SupportItem(
                        title = stringResource(id = R.string.about_option_terms),
                        description = "",
                        icon = Icons.AutoMirrored.Filled.Assignment,
                        onClick = onTermsClick
                    )
                    
                    SupportItem(
                        title = stringResource(id = R.string.about_option_privacy),
                        description = "",
                        icon = Icons.Default.GppGood,
                        onClick = onPrivacyClick
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AboutAppScreenPreview() {
    EvoFitTheme {
        AboutAppContent(
            onBackClick = {},
            onTermsClick = {},
            onPrivacyClick = {}
        )
    }
}
