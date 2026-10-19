package com.guaracode.evofit.presentation.ui.feature.evo.analytics.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.ic_balance_2
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

data class MuscleGroup(
    val name: String,
    val imageRes: DrawableResource? = null
)

@Composable
fun MuscleGroupCard(
    group: MuscleGroup,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface,
        label = "containerColor"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
        label = "contentColor"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.EvoCardHeightLarge)
            .clip(RoundedCornerShape(Dimens.CornerRadiusCard))
            .clickable { onClick() }
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = Dimens.BorderWidthThin,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(Dimens.CornerRadiusCard)
                    )
                } else Modifier
            ),
        shape = RoundedCornerShape(Dimens.CornerRadiusCard),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (group.imageRes != null) {
                    Icon(
                        painter = painterResource(group.imageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(Dimens.SpacingSmall)
                            .fillMaxSize(),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(containerColor.copy(alpha = 0.8f))
                    .padding(vertical = Dimens.SpacingSmall),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = group.name,
                    color = contentColor,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview
@Composable
private fun MuscleGroupCardPreview() {
    EvoFitTheme {
        Row(
            modifier = Modifier.padding(Dimens.SpacingMedium),
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
        ) {
            MuscleGroupCard(
                group = MuscleGroup("Costas", Res.drawable.ic_balance_2),
                isSelected = true,
                onClick = {},
                modifier = Modifier.weight(1f)
            )
            MuscleGroupCard(
                group = MuscleGroup("Peito"),
                isSelected = false,
                onClick = {},
                modifier = Modifier.weight(1f)
            )
        }
    }
}
