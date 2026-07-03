package com.example.evofit.presentation.ui.feature.workout.components.training

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.presentation.ui.feature.workout.components.ExercisePreviewItem

@Composable
fun ExercisePreviewCard(
    index: Int,
    item: ExercisePreviewItem
) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$index",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                val detailText = when (item.unit) {
                    MeasurementUnit.WEIGHT -> {
                        val weightStr = if (item.weight % 1 == 0.0) "${item.weight.toInt()}" else "${item.weight}"
                        "$weightStr kg × ${item.reps} reps"
                    }
                    MeasurementUnit.DISTANCE -> {
                        val distanceStr = if ((item.distance ?: 0.0) % 1 == 0.0) "${item.distance?.toInt()}" else "${item.distance}"
                        "$distanceStr km × ${item.time ?: 0} min"
                    }
                    MeasurementUnit.TIME -> {
                        "${item.time ?: 0} min"
                    }
                    MeasurementUnit.REPS -> {
                        "${item.reps} reps"
                    }
                }
                Text(
                    text = stringResource(
                        R.string.main_workout_exercise_series_format,
                        stringResource(R.string.main_workout_series_count, item.setsCount),
                        detailText
                    ),
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 14.sp
                )
            }
        }
    }
}
