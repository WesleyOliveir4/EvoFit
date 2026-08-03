package com.example.evofit.presentation.ui.feature.workout.components.configure

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.evofit.presentation.model.ExerciseSelectionUIModel
import com.example.evofit.presentation.model.MuscleGroupItem
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme

/**
 * Card de seleção de grupo muscular, usado em [com.example.evofit.presentation.ui.feature.workout.createworkout.screens.NewWorkoutScreen].
 */
@Composable
fun MuscleGroupCard(
    item: MuscleGroupItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
        label = "borderColor"
    )
    val borderWidth by animateDpAsState(
        targetValue = if (isSelected) 2.dp else 1.dp,
        label = "borderWidth"
    )
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.04f) else MaterialTheme.colorScheme.surface,
        label = "containerColor"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(Dimens.CornerRadiusDefault)
            )
            .clip(RoundedCornerShape(Dimens.CornerRadiusDefault))
            .clickable { onClick() },
        shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = Dimens.ScreenPaddingHorizontal, vertical = Dimens.SpacingMediumSmall)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
            ) {
                Box(
                    modifier = Modifier
                        .size(Dimens.SpacingExtraExtraLarge)
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(Dimens.SpacingMediumSmall)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.temporaryIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(Dimens.IconSizeDefault)
                    )
                }

                Text(
                    text = item.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Box(
                modifier = Modifier
                    .size(Dimens.IconSizeDefault)
                    .background(
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                this@Row.AnimatedVisibility(
                    visible = isSelected,
                    enter = expandIn(expandFrom = Alignment.Center),
                    exit = shrinkOut(shrinkTowards = Alignment.Center)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(Dimens.SpacingMedium)
                    )
                }
                if (!isSelected) {
                    Box(
                        modifier = Modifier
                            .size(Dimens.IconSizeSmall)
                            .background(MaterialTheme.colorScheme.surface, CircleShape)
                    )
                }
            }
        }
    }
}

/**
 * Linha de exercício selecionável, usada em [com.example.evofit.presentation.ui.feature.workout.createworkout.screens.SelectExercisesScreen].
 */
@Composable
fun ExerciseRowItem(
    item: ExerciseSelectionUIModel,
    isSelected: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
        label = "borderColor"
    )
    val borderWidth by animateDpAsState(
        targetValue = if (isSelected) 2.dp else 1.dp,
        label = "borderWidth"
    )
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.04f) else MaterialTheme.colorScheme.surface,
        label = "containerColor"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(Dimens.CornerRadiusDefault)
            )
            .clip(RoundedCornerShape(Dimens.CornerRadiusDefault))
            .clickable { onCheckedChange(!isSelected) },
        shape = RoundedCornerShape(Dimens.CornerRadiusDefault),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = Dimens.ScreenPaddingHorizontal, vertical = Dimens.SpacingLarge)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
        ) {
            Box(
                modifier = Modifier
                    .size(Dimens.IconSizeDefault)
                    .background(
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                this@Row.AnimatedVisibility(
                    visible = isSelected,
                    enter = expandIn(expandFrom = Alignment.Center),
                    exit = shrinkOut(shrinkTowards = Alignment.Center)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(Dimens.SpacingMedium)
                    )
                }
                if (!isSelected) {
                    Box(
                        modifier = Modifier
                            .size(Dimens.IconSizeSmall)
                            .background(MaterialTheme.colorScheme.surface, CircleShape)
                    )
                }
            }

            Text(
                text = item.name,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Indicador segmentado de progresso entre páginas de exercícios, usado em
 * [com.example.evofit.presentation.ui.feature.workout.createworkout.screens.ConfigureWorkoutScreen].
 */
@Composable
fun ExercisePageSegmentedIndicator(
    totalCount: Int,
    currentIndex: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.ScreenPaddingHorizontal, vertical = Dimens.SpacingSmall),
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingTiny)
    ) {
        for (i in 0 until totalCount) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(Dimens.SpacingExtraSmall)
                    .background(
                        color = if (i <= currentIndex) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = RoundedCornerShape(Dimens.SpacingExtraExtraSmall)
                    )
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun ExerciseSelectionComponentsPreview() {
    EvoFitTheme {
        Column(
            modifier = Modifier.padding(Dimens.SpacingMedium),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
        ) {
            MuscleGroupCard(
                item = MuscleGroupItem("1", "Peito", Icons.Default.FitnessCenter),
                isSelected = true,
                onClick = {}
            )
            
            ExerciseRowItem(
                item = ExerciseSelectionUIModel("1", "Supino Reto"),
                isSelected = true,
                onCheckedChange = {}
            )

            ExerciseRowItem(
                item = ExerciseSelectionUIModel("2", "Supino Inclinado"),
                isSelected = false,
                onCheckedChange = {}
            )
            
            ExercisePageSegmentedIndicator(
                totalCount = 5,
                currentIndex = 2
            )
        }
    }
}
