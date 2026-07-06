package com.example.evofit.presentation.ui.feature.workout.resume.components

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.presentation.ui.theme.AppGreen
import com.example.evofit.presentation.ui.theme.AppSurface
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import com.example.evofit.presentation.ui.theme.IconContainerBg
import com.example.evofit.presentation.ui.theme.TextPrimary
import com.example.evofit.presentation.ui.theme.TextSecondary

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
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = AppSurface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = stringResource(R.string.workout_resume_label_summary),
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            ResumeRowItem(
                icon = Icons.AutoMirrored.Filled.List,
                label = stringResource(R.string.workout_resume_label_exercises),
                value = "$totalExercises"
            )

            val setsValue = if (completedSets != null) {
                stringResource(R.string.workout_resume_value_sets_format, completedSets, totalSets)
            } else {
                "$totalSets"
            }

            ResumeRowItem(
                icon = Icons.Default.Refresh,
                label = stringResource(R.string.workout_resume_label_sets),
                value = setsValue
            )

            if (duration != null) {
                ResumeRowItem(
                    icon = Icons.Default.Refresh, // You might want a timer icon here
                    label = stringResource(R.string.workout_resume_label_duration),
                    value = duration
                )
            }

            ResumeRowItem(
                icon = Icons.Default.DateRange,
                label = if (duration != null) {
                    stringResource(R.string.workout_resume_label_finished_at)
                } else {
                    stringResource(R.string.workout_resume_label_created_at)
                },
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
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(IconContainerBg, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AppGreen,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = label,
                color = TextSecondary,
                fontSize = 14.sp
            )
            Text(
                text = value,
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun WorkoutSummaryCardPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            WorkoutSummaryCard(
                totalExercises = 5,
                totalSets = 15,
                formattedDate = "25/05/2024"
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun ResumeRowItemPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            ResumeRowItem(
                icon = Icons.AutoMirrored.Filled.List,
                label = "Exercícios",
                value = "5"
            )
        }
    }
}
