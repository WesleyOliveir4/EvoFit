package com.example.evofit.presentation.ui.feature.workout.components.configure

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.example.evofit.R
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme

/**
 * Componente de entrada de peso usado em telas de configuração de treino. Abre um diálogo
 * com o seletor giratório ([WeightWheel]) ao ser tocado.
 */
@Composable
fun WeightWheelSelector(
    modifier: Modifier = Modifier,
    initialWeight: Double,
    onWeightSelected: (Double) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    CompactWeightDisplay(
        weight = initialWeight,
        modifier = modifier
            .fillMaxWidth()
            .clickable { showDialog = true }
    )

    if (showDialog) {
        WeightPickerDialog(
            initialWeight = initialWeight,
            onConfirm = { newWeight ->
                onWeightSelected(newWeight)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun CompactWeightDisplay(
    weight: Double,
    modifier: Modifier = Modifier
) {
    val weightStr = if (weight % 1 == 0.0) "${weight.toInt()}" else "$weight"
    val prevWeight = weight - 1.0
    val nextWeight = weight + 1.0
    val prevStr = if (prevWeight >= 0) (if (prevWeight % 1 == 0.0) "${prevWeight.toInt()}" else "$prevWeight") else ""
    val nextStr = if (nextWeight % 1 == 0.0) "${nextWeight.toInt()}" else "$nextWeight"

    Box(
        modifier = modifier
            .widthIn(max = Dimens.OnboardingLogoSize * 2) // ~240.dp
            .fillMaxWidth()
            .height(Dimens.ButtonHeightSecondary)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(Dimens.SpacingMediumSmall))
            .clip(RoundedCornerShape(Dimens.SpacingMediumSmall)),
        contentAlignment = Alignment.Center
    ) {
        if (prevStr.isNotEmpty()) {
            Text(
                text = prevStr,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = Dimens.SpacingSmall)
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        layout(placeable.width, placeable.height) {
                            placeable.placeRelative(-(placeable.width * 0.6f).toInt(), 0)
                        }
                    }
            )
        }

        Box(
            modifier = Modifier
                .width(Dimens.FabSizeLarge)
                .height(Dimens.AuthBadgeSizeDefault)
                .border(Dimens.BorderWidthThin + Dimens.SpacingExtraExtraSmall / 4, MaterialTheme.colorScheme.primary, RoundedCornerShape(Dimens.SpacingSmall)) // ~1.5dp
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(Dimens.SpacingSmall)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = weightStr,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraBold)
            )
        }

        Text(
            text = nextStr,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = Dimens.SpacingSmall)
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(placeable.width, placeable.height) {
                        placeable.placeRelative((placeable.width * 0.6f).toInt(), 0)
                    }
                }
        )
    }
}

@Composable
fun WeightPickerDialog(
    initialWeight: Double,
    onConfirm: (Double) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedWeight by remember { mutableStateOf(initialWeight) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingMedium),
            shape = RoundedCornerShape(Dimens.CornerRadiusCard),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(Dimens.SpacingLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.configure_workout_col_weight),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

                WeightWheel(
                    initialWeight = initialWeight,
                    onWeightSelected = { selectedWeight = it },
                    isExpanded = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(Dimens.SpacingLarge))

                Button(
                    onClick = { onConfirm(selectedWeight) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.ButtonHeightPrimary),
                    shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(Dimens.SpacingSmall))
                    Text(
                        text = "Confirmar",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun CompactWeightDisplayPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.padding(Dimens.SpacingMedium)) {
            CompactWeightDisplay(weight = 60.0)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun WeightPickerDialogPreview() {
    EvoFitTheme {
        WeightPickerDialog(
            initialWeight = 60.0,
            onConfirm = {},
            onDismiss = {}
        )
    }
}

