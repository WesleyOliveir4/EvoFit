package com.example.evofit.presentation.ui.feature.workout.components.configure

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.R
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme

/**
 * Contador de repetições com botões de incremento/decremento, usado na configuração de séries.
 */
@Composable
fun RepsCounterComponent(
    modifier: Modifier = Modifier,
    value: Int,
    step: Int = 1,
    onValueChange: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .height(Dimens.ButtonHeightSecondary)
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(Dimens.SpacingMediumSmall)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "–",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .clickable { if (value >= step) onValueChange(value - step) else if (value > 0) onValueChange(0) },
            textAlign = TextAlign.Center
        )
        Text(
            text = "$value",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Text(
            text = "+",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .clickable { onValueChange(value + step) },
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Botão tracejado para adicionar uma nova série a um exercício.
 */
@Composable
fun AddSetDashedButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val strokeColor = MaterialTheme.colorScheme.outlineVariant
    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.ButtonHeightSecondary)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRoundRect(
                color = strokeColor,
                style = stroke,
                cornerRadius = CornerRadius(Dimens.SpacingMediumSmall.toPx(), Dimens.SpacingMediumSmall.toPx())
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(Dimens.SpacingMedium)
            )
            Text(
                text = stringResource(R.string.configure_workout_add_set),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun SetInputComponentsPreview() {
    EvoFitTheme {
        Column(
            modifier = Modifier.padding(Dimens.SpacingMedium),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
        ) {
            RepsCounterComponent(
                value = 10,
                onValueChange = {}
            )
            
            AddSetDashedButton(onClick = {})
        }
    }
}
