package com.guaracode.evofit.presentation.ui.feature.authentication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.guaracode.evofit.presentation.ui.feature.components.TopBarReturn
import com.guaracode.evofit.presentation.ui.theme.Dimens

@Composable
fun LegalContentScreen(
    type: String,
    onBackClick: () -> Unit
) {
    val isTerms = type == "terms"
    val title = if (isTerms) "Termos de Uso" else "Política de Privacidade"
    val lastUpdated = "Última atualização: 25 de maio de 2025"

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            TopBarReturn(
                onBackClick = onBackClick,
                title = title,
                subtitle = lastUpdated
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Dimens.ScreenPaddingHorizontal)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(Dimens.SpacingLarge))

            if (isTerms) {
                TermsOfUseContent()
            } else {
                PrivacyPolicyContent()
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingExtraExtraLarge))
        }
    }
}

@Composable
private fun TermsOfUseContent() {
    LegalSection(
        number = "1",
        title = "Sobre o EvoFit",
        content = "O EvoFit é um aplicativo desenvolvido para auxiliar no acompanhamento e na evolução dos seus treinos..."
    )
    LegalSection(
        number = "2",
        title = "Uso do aplicativo",
        content = "Ao utilizar o EvoFit, você concorda em utilizar o aplicativo de forma adequada..."
    )
}

@Composable
private fun PrivacyPolicyContent() {
    LegalSection(
        number = "1",
        title = "Quais dados coletamos",
        content = "Para fornecer as funcionalidades do EvoFit, podemos armazenar informações..."
    )
}

@Composable
private fun LegalSection(
    number: String,
    title: String,
    content: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.SpacingMediumSmall)
    ) {
        Text(
            text = "$number. $title",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
