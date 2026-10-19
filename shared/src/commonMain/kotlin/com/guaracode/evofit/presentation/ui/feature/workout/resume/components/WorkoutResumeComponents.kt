package com.guaracode.evofit.presentation.ui.feature.workout.resume.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme

@Composable
fun WorkoutSummaryCard(
    totalExercises: Int,
    totalSets: Int,
    completedSets: Int? = null,
    duration: String? = null,
    formattedDate: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.CornerRadiusCard),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingLarge),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
        ) {
            Text(
                text = "RESUMO DO TREINO",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            ResumeRowItem(
                icon = Icons.AutoMirrored.Filled.List,
                label = "Exercícios",
                value = "$totalExercises"
            )

            val setsValue = if (completedSets != null) {
                "$completedSets de $totalSets séries"
            } else {
                "$totalSets"
            }

            ResumeRowItem(
                icon = Icons.Default.Refresh,
                label = "Séries",
                value = setsValue
            )

            ResumeRowItem(
                icon = Icons.Default.DateRange,
                label = if (duration != null) "Finalizado em" else "Criado em",
                value = formattedDate
            )
        }
    }
}

@Composable
fun ResumeRowItem(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.SpacingExtraExtraLarge)
                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(Dimens.IconSizeSmall)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingExtraExtraSmall)
        ) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = value,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Preview
@Composable
private fun WorkoutSummaryCardPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.padding(Dimens.SpacingMedium)) {
            WorkoutSummaryCard(
                totalExercises = 5,
                totalSets = 15,
                formattedDate = "25/05/2026"
            )
        }
    }
}
