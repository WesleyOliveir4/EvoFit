package com.example.evofit.presentation.ui.feature.evo.analytics.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme

data class MuscleGroup(
    val name: String,
    val imageRes: Int? = null
)

@Composable
fun MuscleGroupCard(
    group: MuscleGroup,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor by androidx.compose.animation.animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface,
        label = "containerColor"
    )
    val contentColor by androidx.compose.animation.animateColorAsState(
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
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (group.imageRes != null) {
                    Image(
                        painter = painterResource(id = group.imageRes),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
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

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun MuscleGroupCardPreview() {
    EvoFitTheme {
        Row(
            modifier = Modifier.padding(Dimens.SpacingMedium),
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
        ) {
            MuscleGroupCard(
                group = MuscleGroup("Costas", com.example.evofit.R.drawable.img_back),
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
