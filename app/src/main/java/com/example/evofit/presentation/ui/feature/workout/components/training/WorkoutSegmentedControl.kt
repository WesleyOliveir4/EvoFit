package com.example.evofit.presentation.ui.feature.workout.components.training

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import com.example.evofit.presentation.ui.theme.IconContainerBg

@Composable
fun WorkoutSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Cores obtidas do tema
    val backgroundColor = MaterialTheme.colorScheme.surface
    val selectedItemBg = IconContainerBg
    val selectedTextColor = MaterialTheme.colorScheme.primary
    val unselectedTextColor = MaterialTheme.colorScheme.secondary

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.ButtonHeightSecondary)
            .clip(RoundedCornerShape(Dimens.CornerRadiusDefault))
            .background(backgroundColor)
            .padding(Dimens.SpacingExtraSmall)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEachIndexed { index, title ->
                val isSelected = index == selectedIndex

                val itemBgColor by animateColorAsState(
                    targetValue = if (isSelected) selectedItemBg else Color.Transparent,
                    animationSpec = tween(durationMillis = 200),
                    label = "bgColorAnimation"
                )
                val textColor by animateColorAsState(
                    targetValue = if (isSelected) selectedTextColor else unselectedTextColor,
                    animationSpec = tween(durationMillis = 200),
                    label = "textColorAnimation"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(Dimens.SpacingMediumSmall))
                        .background(itemBgColor)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onOptionSelected(index)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun WorkoutSegmentedControlPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.padding(Dimens.SpacingMedium)) {
            WorkoutSegmentedControl(
                options = listOf("Meus treinos", "Histórico"),
                selectedIndex = 0,
                onOptionSelected = {}
            )
        }
    }
}
