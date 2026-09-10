package com.example.evofit.presentation.ui.feature.profile.goals.components

import android.content.res.Configuration
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.presentation.ui.theme.AppDarkBg
import com.example.evofit.presentation.ui.theme.AppGreen
import com.example.evofit.presentation.ui.theme.AppSurface
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoBlue
import com.example.evofit.presentation.ui.theme.EvoDestructiveRed
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import com.example.evofit.presentation.ui.theme.EvoGreen
import com.example.evofit.presentation.ui.theme.EvoPurple
import com.example.evofit.presentation.ui.theme.TextPrimary
import com.example.evofit.presentation.ui.theme.TextSecondary

@Composable
fun GoalCard(
    title: String,
    category: String,
    currentValue: String,
    targetValue: String,
    percentage: Int,
    iconRes: Int? = null,
    initiallyExpanded: Boolean = false,
    onDeleteClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }

    val categoryColors = when (category) {
        "Força" -> Pair(EvoPurple.copy(alpha = 0.15f), EvoPurple)
        "Cardio" -> Pair(EvoGreen.copy(alpha = 0.15f), EvoGreen)
        "Peso" -> Pair(EvoBlue.copy(alpha = 0.15f), EvoBlue)
        else -> Pair(Color(0xFF261A35), EvoPurple)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppSurface)
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
                // Ícone da Meta
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(categoryColors.first, RoundedCornerShape(Dimens.SpacingMediumSmall)),
                    contentAlignment = Alignment.Center
                ) {
                    if (iconRes != null) {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = null,
                            tint = categoryColors.second,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Conteúdo Principal
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
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )

                            // Tag da Categoria
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

                    // Progress Bar contínua
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
                // Info Detalhada
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Meta
                    GoalDetailItem(
                        icon = Icons.Default.Adjust,
                        label = "Meta",
                        value = targetValue
                    )
                    
                    // Atual
                    val currentLabel = when (category) {
                        "Cardio" -> "Distância atual"
                        "Peso" -> "Peso atual"
                        else -> "Atual"
                    }
                    GoalDetailItem(
                        icon = Icons.Default.Route,
                        label = currentLabel,
                        value = currentValue
                    )
                }

                // Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { /* Sem função por hora */ },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = AppGreen),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Finalizar", color = Color.Black, fontWeight = FontWeight.Bold)
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
                        Text("Excluir", color = Color.White, fontWeight = FontWeight.Bold)
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
            tint = TextSecondary,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            color = TextPrimary,
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
    val filters = listOf("Todas", "Força", "Cardio", "Peso")
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
                color = if (isSelected) AppGreen else AppSurface,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(40.dp)
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = filter,
                        color = if (isSelected) Color.Black else TextSecondary,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Preview(name = "Força", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun StrengthGoalPreview() {
    EvoFitTheme {
        Surface(color = AppDarkBg) {
            GoalCard(
                title = "Supino reto 100kg",
                category = "Força",
                currentValue = "90kg",
                targetValue = "100kg",
                percentage = 90,
                iconRes = R.drawable.ic_chest,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(name = "Filtros", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GoalFiltersPreview() {
    EvoFitTheme {
        Surface(color = AppDarkBg) {
            GoalFilterRow(
                selectedFilter = "Todas",
                onFilterSelected = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(name = "Cardio", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CardioGoalPreview() {
    EvoFitTheme {
        Surface(color = AppDarkBg) {
            GoalCard(
                title = "Corrida 5km",
                category = "Cardio",
                currentValue = "2.5km",
                targetValue = "5.0km",
                percentage = 50,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(name = "Peso", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun WeightGoalPreview() {
    EvoFitTheme {
        Surface(color = AppDarkBg) {
            GoalCard(
                title = "Peso Corporal",
                category = "Peso",
                currentValue = "82kg",
                targetValue = "75kg",
                percentage = 30,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(name = "Expandido", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GoalCardExpandedPreview() {
    EvoFitTheme {
        Surface(color = AppDarkBg) {
            // Note: GoalCard internally manages 'expanded' state, but we can't force it in Preview easily
            // unless we modify GoalCard to accept an expanded state. 
            // For now, let's just make sure the code compiles.
            // If I want to see it expanded in preview, I'd need to add 'initiallyExpanded' parameter.
            GoalCard(
                title = "Corrida 5km",
                category = "Cardio",
                currentValue = "2.5km em 15m",
                targetValue = "5.0km em 25m",
                percentage = 50,
                iconRes = R.drawable.ic_person_running,
                initiallyExpanded = true,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
