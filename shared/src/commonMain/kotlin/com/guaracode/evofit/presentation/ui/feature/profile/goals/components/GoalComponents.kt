package com.guaracode.evofit.presentation.ui.feature.profile.goals.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoBlue
import com.guaracode.evofit.presentation.ui.theme.EvoDestructiveRed
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import com.guaracode.evofit.presentation.ui.theme.EvoGreen
import com.guaracode.evofit.presentation.ui.theme.EvoPurple
import com.guaracode.evofit.presentation.ui.theme.evoColors
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.goal_category_label_cardio
import evofit.shared.generated.resources.goal_category_label_strength
import evofit.shared.generated.resources.goal_category_label_weight
import evofit.shared.generated.resources.profile_goals_button_delete
import evofit.shared.generated.resources.profile_goals_button_finish
import evofit.shared.generated.resources.profile_goals_filter_all
import evofit.shared.generated.resources.profile_goals_label_distance_current
import evofit.shared.generated.resources.profile_goals_label_goal
import evofit.shared.generated.resources.profile_goals_label_weight_current
import evofit.shared.generated.resources.profile_goals_label_current_simple
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun GoalCard(
    title: String,
    category: String,
    currentValue: String,
    targetValue: String,
    percentage: Int,
    iconRes: DrawableResource? = null,
    initiallyExpanded: Boolean = false,
    onDeleteClick: () -> Unit = {},
    onFinishClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val strengthLabel = stringResource(Res.string.goal_category_label_strength)
    val cardioLabel = stringResource(Res.string.goal_category_label_cardio)
    val weightLabel = stringResource(Res.string.goal_category_label_weight)

    var expanded by remember { mutableStateOf(initiallyExpanded) }

    val categoryColors = when (category) {
        strengthLabel -> Pair(EvoPurple.copy(alpha = 0.15f), EvoPurple)
        cardioLabel -> Pair(EvoGreen.copy(alpha = 0.15f), EvoGreen)
        weightLabel -> Pair(EvoBlue.copy(alpha = 0.15f), EvoBlue)
        else -> Pair(Color(0xFF261A35), EvoPurple)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(categoryColors.first, RoundedCornerShape(Dimens.SpacingMediumSmall)),
                    contentAlignment = Alignment.Center
                ) {
                    if (iconRes != null) {
                        Icon(
                            painter = painterResource(iconRes),
                            contentDescription = null,
                            tint = categoryColors.second,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = title,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Surface(
                                color = categoryColors.first,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = category,
                                    color = categoryColors.second,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "$percentage%",
                                color = categoryColors.second,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(categoryColors.first)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(percentage / 100f)
                                .fillMaxHeight()
                                .background(categoryColors.second)
                        )
                    }
                }
            }

            if (expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GoalDetailItem(
                        icon = Icons.Default.Adjust,
                        label = stringResource(Res.string.profile_goals_label_goal),
                        value = targetValue
                    )
                    
                    val currentLabel = when (category) {
                        cardioLabel -> stringResource(Res.string.profile_goals_label_distance_current)
                        weightLabel -> stringResource(Res.string.profile_goals_label_weight_current)
                        else -> stringResource(Res.string.profile_goals_label_current_simple)
                    }
                    GoalDetailItem(
                        icon = Icons.Default.Route,
                        label = currentLabel,
                        value = currentValue
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { onFinishClick() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.evoColors.green),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(Res.string.profile_goals_button_finish),
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Button(
                        onClick = { onDeleteClick() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = EvoDestructiveRed),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(Res.string.profile_goals_button_delete),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GoalDetailItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = label,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun GoalFilterRow(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val filters = listOf(
        stringResource(Res.string.profile_goals_filter_all),
        stringResource(Res.string.goal_category_label_strength),
        stringResource(Res.string.goal_category_label_cardio),
        stringResource(Res.string.goal_category_label_weight)
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { filter ->
            val isSelected = selectedFilter == filter
            Surface(
                onClick = { onFilterSelected(filter) },
                color = if (isSelected) MaterialTheme.evoColors.green else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(40.dp)
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = filter,
                        color = if (isSelected) Color.Black else MaterialTheme.colorScheme.secondary,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun StrengthGoalPreview() {
    EvoFitTheme {
        GoalCard(
            title = "Supino reto 100kg",
            category = "Força",
            currentValue = "90kg",
            targetValue = "100kg",
            percentage = 90,
            modifier = Modifier.padding(16.dp)
        )
    }
}
