package com.guaracode.evofit.presentation.ui.feature.profile.support.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.guaracode.evofit.presentation.ui.feature.profile.support.components.FAQItem
import com.guaracode.evofit.presentation.ui.feature.profile.support.tracking.SupportTracker
import com.guaracode.evofit.presentation.ui.feature.profile.support.utils.FAQContext
import com.guaracode.evofit.presentation.ui.feature.profile.support.utils.FAQItemData
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.faq_search_placeholder
import evofit.shared.generated.resources.faq_section_title
import evofit.shared.generated.resources.faq_title
import evofit.shared.generated.resources.new_workout_back_desc
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun FAQScreen(
    onBackClick: () -> Unit = {},
    tracker: SupportTracker = koinInject()
) {
    LaunchedEffect(Unit) {
        tracker.trackFAQScreenView()
    }
    val faqItems = FAQContext.getFAQItems()
    var searchQuery by remember { mutableStateOf("") }

    val filteredItems = remember(searchQuery, faqItems) {
        if (searchQuery.isEmpty()) {
            faqItems
        } else {
            faqItems.filter {
                it.question.contains(searchQuery, ignoreCase = true) ||
                it.answer.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    FAQContent(
        faqItems = filteredItems,
        searchQuery = searchQuery,
        onSearchQueryChange = { searchQuery = it },
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAQContent(
    faqItems: List<FAQItemData>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.faq_title),
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
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Dimens.SpacingSmall),
                    placeholder = { 
                        Text(
                            text = stringResource(Res.string.faq_search_placeholder),
                            style = MaterialTheme.typography.bodyMedium
                        ) 
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(Dimens.IconSizeDefault),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    },
                    shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )
            }

            item {
                Text(
                    text = stringResource(Res.string.faq_section_title),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = Dimens.SpacingSmall)
                )
            }

            items(faqItems, key = { it.id }) { item ->
                FAQItem(
                    question = item.question,
                    answer = item.answer
                )
            }
        }
    }
}

@Preview
@Composable
private fun FAQScreenPreview() {
    EvoFitTheme {
        FAQContent(
            faqItems = listOf(
                FAQItemData(1, "Como criar uma conta?", "Basta clicar em cadastrar."),
                FAQItemData(2, "Como registrar meu treino?", "Clique em iniciar treino.")
            ),
            searchQuery = "",
            onSearchQueryChange = {},
            onBackClick = {}
        )
    }
}
