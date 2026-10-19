package com.guaracode.evofit.presentation.ui.feature.profile.support.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guaracode.evofit.presentation.ui.feature.profile.support.tracking.SupportTracker
import com.guaracode.evofit.presentation.ui.feature.profile.support.utils.SuggestionCategory
import com.guaracode.evofit.presentation.ui.feature.profile.support.viewmodel.SupportEmailViewModel
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.new_workout_back_desc
import evofit.shared.generated.resources.suggestion_workouts_button_send
import evofit.shared.generated.resources.suggestion_workouts_label_category
import evofit.shared.generated.resources.suggestion_workouts_label_description
import evofit.shared.generated.resources.suggestion_workouts_placeholder_category
import evofit.shared.generated.resources.suggestion_workouts_placeholder_description
import evofit.shared.generated.resources.suggestion_workouts_success
import evofit.shared.generated.resources.suggestion_workouts_title
import evofit.shared.generated.resources.support_email_description
import evofit.shared.generated.resources.support_email_message_counter_format
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SuggestionNewWorkoutsScreen(
    viewModel: SupportEmailViewModel = koinViewModel(),
    tracker: SupportTracker = koinInject(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val successMessage = stringResource(Res.string.suggestion_workouts_success)

    LaunchedEffect(Unit) {
        tracker.trackSuggestionScreenView()
    }

    var selectedCategory by remember { mutableStateOf<SuggestionCategory?>(null) }
    var suggestion by remember { mutableStateOf("") }
    
    val selectedCategoryTitle = selectedCategory?.let { stringResource(it.titleRes) } ?: ""

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            snackbarHostState.showSnackbar(successMessage)
            onBackClick()
            viewModel.resetState()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    SuggestionNewWorkoutsContent(
        selectedCategory = selectedCategory,
        suggestion = suggestion,
        isLoading = uiState.isLoading,
        snackbarHostState = snackbarHostState,
        onCategoryChange = { selectedCategory = it },
        onSuggestionChange = { suggestion = it },
        onBackClick = onBackClick,
        onSendClick = {
            if (selectedCategory != null) {
                viewModel.sendEmail("Sugestão: $selectedCategoryTitle", suggestion)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestionNewWorkoutsContent(
    selectedCategory: SuggestionCategory?,
    suggestion: String,
    isLoading: Boolean,
    snackbarHostState: SnackbarHostState,
    onCategoryChange: (SuggestionCategory) -> Unit,
    onSuggestionChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isButtonEnabled = selectedCategory != null && suggestion.isNotBlank() && !isLoading
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.suggestion_workouts_title),
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
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(Dimens.ScreenPaddingHorizontal)
                    .padding(bottom = Dimens.SpacingMedium)
            ) {
                Button(
                    onClick = onSendClick,
                    enabled = isButtonEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.ButtonHeightPrimary),
                    shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(Dimens.IconSizeDefault)
                            )
                            Text(
                                text = stringResource(Res.string.suggestion_workouts_button_send),
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Dimens.ScreenPaddingHorizontal)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge)
        ) {
            Text(
                text = stringResource(Res.string.support_email_description),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyLarge
            )

            // Category Selector
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)) {
                Text(
                    text = stringResource(Res.string.suggestion_workouts_label_category),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium
                )
                
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = selectedCategory?.let { stringResource(it.titleRes) } ?: "",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(stringResource(Res.string.suggestion_workouts_placeholder_category)) },
                        readOnly = true,
                        enabled = false,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        },
                        shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                            disabledBorderColor = Color.Transparent,
                            disabledPlaceholderColor = MaterialTheme.colorScheme.secondary,
                            disabledTextColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { isDropdownExpanded = true }
                    )

                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        SuggestionCategory.entries.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(text = stringResource(category.titleRes)) },
                                onClick = {
                                    onCategoryChange(category)
                                    isDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Suggestion Description Field
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)) {
                Text(
                    text = stringResource(Res.string.suggestion_workouts_label_description),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium
                )
                
                OutlinedTextField(
                    value = suggestion,
                    onValueChange = { if (it.length <= 1000) onSuggestionChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    placeholder = { Text(stringResource(Res.string.suggestion_workouts_placeholder_description)) },
                    shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )
                
                Text(
                    text = stringResource(Res.string.support_email_message_counter_format, suggestion.length),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingLarge))
        }
    }
}

@Preview
@Composable
private fun SuggestionNewWorkoutsScreenPreview() {
    EvoFitTheme {
        SuggestionNewWorkoutsContent(
            selectedCategory = SuggestionCategory.NEW_WORKOUT,
            suggestion = "",
            isLoading = false,
            snackbarHostState = remember { SnackbarHostState() },
            onCategoryChange = {},
            onSuggestionChange = {},
            onBackClick = {},
            onSendClick = {}
        )
    }
}
