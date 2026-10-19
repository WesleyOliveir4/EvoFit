package com.guaracode.evofit.presentation.ui.feature.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.guaracode.evofit.core.common.DateMapper
import com.guaracode.evofit.presentation.ui.theme.AppGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvoDatePickerDialog(
    initialDate: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = DateMapper.parseDateUtc(initialDate) ?: 0L
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { millis ->
                    onConfirm(DateMapper.formatDateUtc(millis))
                }
                onDismiss()
            }) {
                Text("Confirmar", color = AppGreen)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Gray)
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
