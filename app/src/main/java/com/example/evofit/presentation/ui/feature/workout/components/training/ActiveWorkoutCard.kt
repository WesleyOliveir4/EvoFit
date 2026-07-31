package com.example.evofit.presentation.ui.feature.workout.components.training

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.R
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme

@Composable
fun ActiveWorkoutCard(
    workoutName: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = Dimens.BorderWidthThin,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(Dimens.CornerRadiusDefault)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
    ) {
        Row(
            modifier = Modifier
                .padding(Dimens.SpacingMediumSmall)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Dimens.OnboardingIconSize / 2) // 40.dp
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), RoundedCornerShape(Dimens.SpacingMediumSmall)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(Dimens.StatCardIconSize)
                )
            }

            Spacer(modifier = Modifier.width(Dimens.SpacingMedium))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = workoutName,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.main_workout_active_session_resume),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun ActiveWorkoutCardPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.padding(Dimens.SpacingMedium)) {
            ActiveWorkoutCard(
                workoutName = "Treino de Peito",
                onClick = {}
            )
        }
    }
}
