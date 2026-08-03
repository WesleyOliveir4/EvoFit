package com.example.evofit.presentation.ui.feature.evo.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.R
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoDestructiveRed
import com.example.evofit.presentation.ui.theme.evoColors
import com.example.evofit.presentation.ui.theme.EvoFitTheme

@Composable
fun StrengthProgressRow(
    position: String,
    exerciseName: String,
    progressValue: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = position,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.width(Dimens.EvoRankingPositionWidth)
        )
        
        Text(
            text = exerciseName,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.weight(1f)
        )
        
        Text(
            text = progressValue,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun StrengthGainsCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = Dimens.BorderWidthThin,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(Dimens.CornerRadiusCard)
            ),
        shape = RoundedCornerShape(Dimens.CornerRadiusCard),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingLarge)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_trophy),
                    contentDescription = null,
                    tint = MaterialTheme.evoColors.yellow,
                    modifier = Modifier.size(Dimens.IconSizeSmall)
                )
                Text(
                    text = stringResource(R.string.evo_home_strength_title),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = TextStyle(letterSpacing = Dimens.TextSizeExtraExtraSmall / 20).letterSpacing
                    )
                )
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

            content()
        }
    }
}

@Composable
fun MostEvolvedCard(
    muscleName: String,
    percentage: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(Dimens.EvoCardHeightMedium)
            .border(
                width = Dimens.BorderWidthThin,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(Dimens.CornerRadiusCard)
            ),
        shape = RoundedCornerShape(Dimens.CornerRadiusCard),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.SpacingLarge),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingTiny)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_medal),
                    contentDescription = null,
                    tint = MaterialTheme.evoColors.green,
                    modifier = Modifier.size(Dimens.IconSizeSmall)
                )

                Text(
                    text = stringResource(R.string.evo_home_most_evolved_title),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = TextStyle(letterSpacing = Dimens.TextSizeExtraExtraSmall / 20).letterSpacing
                    )
                )
            }
            
            Column {
                Text(
                    text = muscleName,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = percentage,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
fun WorkoutsCompletedCard(
    count: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(Dimens.EvoCardHeightMedium)
            .border(
                width = Dimens.BorderWidthThin,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(Dimens.CornerRadiusCard)
            ),
        shape = RoundedCornerShape(Dimens.CornerRadiusCard),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.SpacingLarge),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingTiny)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_fire),
                    contentDescription = null,
                    tint = MaterialTheme.evoColors.red,
                    modifier = Modifier.size(Dimens.IconSizeSmall)
                )
                Text(
                    text = stringResource(R.string.evo_home_workouts_title),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                    )
                )
            }
            
            Column {
                Text(
                    text = count,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = stringResource(R.string.evo_home_workouts_completed),
                    color = MaterialTheme.evoColors.red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun LeastTrainedCard(
    muscleName: String,
    sessionsCount: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = Dimens.BorderWidthThin,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(Dimens.CornerRadiusCard)
            ),
        shape = RoundedCornerShape(Dimens.CornerRadiusCard),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.SpacingLarge, vertical = Dimens.SpacingMediumSmall),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpacingTiny)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingTiny)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_alert),
                        contentDescription = null,
                        tint = MaterialTheme.evoColors.orange,
                        modifier = Modifier.size(Dimens.IconSizeSmall)
                    )
                    Text(
                        text = stringResource(R.string.evo_home_least_trained_title),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = TextStyle(letterSpacing = Dimens.TextSizeExtraExtraSmall / 20).letterSpacing
                        )
                    )
                }
                Text(
                    text = muscleName,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            Text(
                text = stringResource(R.string.evo_home_sessions_count, sessionsCount),
                color = MaterialTheme.evoColors.orange,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun KmPerWeekCard(
    kmPerWeek: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = Dimens.BorderWidthThin,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(Dimens.CornerRadiusCard)
            ),
        shape = RoundedCornerShape(Dimens.CornerRadiusCard),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.SpacingLarge, vertical = Dimens.SpacingMediumSmall),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpacingTiny)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingTiny)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_person_running),
                        contentDescription = null,
                        tint = MaterialTheme.evoColors.blue,
                        modifier = Modifier.size(Dimens.IconSizeSmall)
                    )
                    Text(
                        text = stringResource(R.string.evo_home_km_per_week_title),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = TextStyle(letterSpacing = Dimens.TextSizeExtraExtraSmall / 20).letterSpacing
                        )
                    )
                }
                Text(
                    text = stringResource(
                        R.string.evo_home_km_value,
                        String.format("%.1f", kmPerWeek).replace('.', ',')
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            Text(
                text = stringResource(R.string.evo_home_average_label),
                color = MaterialTheme.evoColors.blue,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Composable
fun AverageWorkoutTimeCard(
    averageTimeMinutes: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = Dimens.BorderWidthThin,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(Dimens.CornerRadiusCard)
            ),
        shape = RoundedCornerShape(Dimens.CornerRadiusCard),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.SpacingLarge, vertical = Dimens.SpacingMediumSmall),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpacingTiny)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingTiny)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_hourglass),
                        contentDescription = null,
                        tint = MaterialTheme.evoColors.green,
                        modifier = Modifier.size(Dimens.IconSizeSmall)
                    )
                    Text(
                        text = stringResource(R.string.evo_home_avg_time_title),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = TextStyle(letterSpacing = Dimens.TextSizeExtraExtraSmall / 20).letterSpacing
                        )
                    )
                }
                Text(
                    text = stringResource(R.string.evo_home_time_value, averageTimeMinutes),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            Text(
                text = stringResource(R.string.evo_home_per_session_label),
                color = MaterialTheme.evoColors.green,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Composable
fun ExerciseAnalyticsCard(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimens.CornerRadiusCard))
            .clickable { onClick() }
            .border(
                width = Dimens.BorderWidthThin,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(Dimens.CornerRadiusCard)
            ),
        shape = RoundedCornerShape(Dimens.CornerRadiusCard),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingLarge),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
        ) {
            Box(
                modifier = Modifier
                    .size(Dimens.MinimumTouchTarget)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(Dimens.CornerRadiusSmall)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_tabler_presentation_analytics),
                    contentDescription = null,
                    tint = MaterialTheme.evoColors.green,
                    modifier = Modifier.size(Dimens.IconSizeLarge)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(Dimens.SpacingExtraSmall)
            ) {
                Text(
                    text = stringResource(R.string.evo_home_exercise_analysis_card_title),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = stringResource(R.string.evo_home_exercise_analysis_card_desc),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodySmall,
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun ExerciseAnalysisCardPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.padding(Dimens.SpacingMedium)) {
            ExerciseAnalyticsCard()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun StrengthGainsCardPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.padding(Dimens.SpacingMedium)) {
            StrengthGainsCard {
                StrengthProgressRow(position = "1º", exerciseName = "Supino Reto", progressValue = "+12kg")
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = Dimens.BorderWidthThin / 2, modifier = Modifier.padding(vertical = Dimens.SpacingMediumSmall))
                StrengthProgressRow(position = "2º", exerciseName = "Agachamento", progressValue = "+10kg")
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun MostEvolvedCardPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.padding(Dimens.SpacingMedium)) {
            MostEvolvedCard(muscleName = "Peitoral", percentage = "+18%")
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun WorkoutsCompletedCardPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.padding(Dimens.SpacingMedium)) {
            WorkoutsCompletedCard(count = "24")
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun LeastTrainedCardPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.padding(Dimens.SpacingMedium)) {
            LeastTrainedCard(muscleName = "Pernas", sessionsCount = 2)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun KmPerWeekCardPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.padding(Dimens.SpacingMedium)) {
            KmPerWeekCard(kmPerWeek = 12.5)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun AverageWorkoutTimeCardPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.padding(Dimens.SpacingMedium)) {
            AverageWorkoutTimeCard(averageTimeMinutes = 45)
        }
    }
}
