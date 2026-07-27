package com.example.evofit.presentation.ui.feature.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.example.evofit.R
import com.example.evofit.core.common.DateMapper
import com.example.evofit.presentation.ui.theme.AppGreen
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvoDatePickerDialog(
    initialDate: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = DateMapper.parseDateUtc(initialDate) ?: System.currentTimeMillis()
    )

    val configuration = LocalConfiguration.current
    val ptBrLocale = Locale("pt", "BR")
    val localizedConfiguration = remember(configuration) {
        android.content.res.Configuration(configuration).apply {
            setLocale(ptBrLocale)
        }
    }

    CompositionLocalProvider(LocalConfiguration provides localizedConfiguration) {
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onConfirm(DateMapper.formatDateUtc(millis))
                    }
                    onDismiss()
                }) {
                    Text(stringResource(R.string.confirm), color = AppGreen)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.cancel), color = Color.Gray)
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = AppGreen,
                    selectedDayContentColor = Color.Black,
                    todayContentColor = AppGreen,
                    todayDateBorderColor = AppGreen
                )
            )
        }
    }
}
