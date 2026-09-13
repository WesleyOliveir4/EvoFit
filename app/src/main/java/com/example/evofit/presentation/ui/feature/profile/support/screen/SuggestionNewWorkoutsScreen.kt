package com.example.evofit.presentation.ui.feature.profile.support.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.presentation.ui.theme.AppDarkBg
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import com.example.evofit.presentation.ui.theme.TextPrimary
import com.example.evofit.presentation.ui.theme.TextSecondary

@Composable
fun SuggestionNewWorkoutsScreen(
    onBackClick: () -> Unit = {}
) {
    var category by remember { mutableStateOf("") }
    var suggestion by remember { mutableStateOf("") }

    SuggestionNewWorkoutsContent(
        category = category,
        suggestion = suggestion,
        onCategoryChange = { category = it },
        onSuggestionChange = { suggestion = it },
        onBackClick = onBackClick,
        onSendClick = { /* TODO: Send Logic */ }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestionNewWorkoutsContent(
    category: String,
    suggestion: String,
    onCategoryChange: (String) -> Unit,
    onSuggestionChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppDarkBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.suggestion_workouts_title),
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
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(AppDarkBg)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(Dimens.ScreenPaddingHorizontal)
                    .padding(bottom = Dimens.SpacingMedium)
            ) {
                Button(
                    onClick = onSendClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.ButtonHeightPrimary),
                    shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
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
                            text = stringResource(id = R.string.suggestion_workouts_button_send),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
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
                text = stringResource(id = R.string.support_email_description), // Using same helper text
                color = TextSecondary,
                style = MaterialTheme.typography.bodyLarge
            )

            // Category Selector (Placeholder)
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)) {
                Text(
                    text = stringResource(id = R.string.suggestion_workouts_label_category),
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
                
                OutlinedTextField(
                    value = category,
                    onValueChange = onCategoryChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(stringResource(id = R.string.suggestion_workouts_placeholder_category)) },
                    readOnly = true,
                    enabled = false,
                    shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        disabledBorderColor = Color.Transparent,
                        disabledPlaceholderColor = TextSecondary
                    )
                )
            }

            // Suggestion Description Field
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)) {
                Text(
                    text = stringResource(id = R.string.suggestion_workouts_label_description),
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
                
                OutlinedTextField(
                    value = suggestion,
                    onValueChange = { if (it.length <= 1000) onSuggestionChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    placeholder = { Text(stringResource(id = R.string.suggestion_workouts_placeholder_description)) },
                    shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )
                
                Text(
                    text = stringResource(id = R.string.support_email_message_counter_format, suggestion.length),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingLarge))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SuggestionNewWorkoutsScreenPreview() {
    EvoFitTheme {
        SuggestionNewWorkoutsContent(
            category = "",
            suggestion = "",
            onCategoryChange = {},
            onSuggestionChange = {},
            onBackClick = {},
            onSendClick = {}
        )
    }
}
