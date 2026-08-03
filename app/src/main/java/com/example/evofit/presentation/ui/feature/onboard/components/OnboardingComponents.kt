package com.example.evofit.presentation.ui.feature.onboard.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.R
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme

data class OnboardingPage(
    val title: String,
    val highlightText: String,
    val description: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EvoWheelPicker(
    range: List<Int>,
    unit: String,
    initialValue: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val itemHeight = Dimens.WheelPickerItemHeight
    val visibleItemsCount = 5
    val listState = rememberLazyListState()
    val snapBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val initialIndex = remember {
        val idx = range.indexOf(initialValue)
        if (idx != -1) idx else 0
    }

    LaunchedEffect(Unit) {
        listState.scrollToItem(initialIndex)
    }

    val selectedIndex by remember {
        derivedStateOf { listState.firstVisibleItemIndex }
    }

    LaunchedEffect(selectedIndex) {
        if (selectedIndex < range.size) {
            onValueChange(range[selectedIndex])
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(itemHeight * visibleItemsCount),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight - Dimens.SpacingSmall)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(Dimens.CornerRadiusDefault))
        )

        LazyColumn(
            state = listState,
            flingBehavior = snapBehavior,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = itemHeight * 2)
        ) {
            items(range.size) { index ->
                val isSelected = index == selectedIndex
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${range[index]}",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = if (isSelected) MaterialTheme.typography.displayLarge else MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.alpha(if (isSelected) 1.0f else 0.4f)
                    )
                    Spacer(modifier = Modifier.width(Dimens.SpacingSmall))
                    Text(
                        text = unit,
                        color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.secondary,
                        style = if (isSelected) MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold) else MaterialTheme.typography.bodySmall,
                        modifier = Modifier.alpha(if (isSelected) 1.0f else 0.4f)
                    )
                }
            }
        }
    }
}

@Composable
fun PageIndicators(
    pageCount: Int,
    selectedPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(
                        width = if (selectedPage == index) Dimens.IndicatorWidthActive else Dimens.IndicatorWidthInactive,
                        height = Dimens.IndicatorHeight
                    )
                    .clip(CircleShape)
                    .background(
                        if (selectedPage == index)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.outline
                    )
            )
        }
    }
}

@Composable
fun UserInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
        )

        Spacer(modifier = Modifier.height(Dimens.SpacingSmall))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            shape = RoundedCornerShape(Dimens.CornerRadiusExtraSmall),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}



@Composable
fun GoalTag(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .border(
                width = Dimens.BorderWidthThin,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(Dimens.CornerRadiusSmall)
            )
            .clickable { onClick() }
            .padding(horizontal = Dimens.SpacingSmall, vertical = Dimens.SpacingExtraSmall)
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onBackground,
            style = TextStyle(fontSize = Dimens.TextSizeExtraExtraSmall)
        )
    }
}

@Composable
fun ActiveGoalItem(
    text: String,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.ButtonHeightPrimary)
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(Dimens.CornerRadiusMedium))
            .padding(horizontal = Dimens.SpacingMediumSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(Dimens.IconSizeSmall)
            )
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
            )
        }

        IconButton(onClick = onRemoveClick) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = stringResource(R.string.onboarding_component_remove_goal),
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(Dimens.IconSizeSmall)
            )
        }
    }
}

@Composable
fun AddNewGoalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val stroke = Stroke(
        width = 6f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )

    val strokeColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.ButtonHeightPrimary)
            .clip(RoundedCornerShape(Dimens.CornerRadiusSmall))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRoundRect(
                color = strokeColor,
                style = stroke,
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(Dimens.CornerRadiusMedium.toPx(), Dimens.CornerRadiusMedium.toPx())
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(Dimens.IconSizeMediumSmall)
            )
            Text(
                text = stringResource(R.string.onboarding_component_add_new_goal),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
fun GoalTagPreview() {
    EvoFitTheme {
        GoalTag(text = "Ganhar massa")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
fun ActiveGoalItemPreview() {
    EvoFitTheme {
        ActiveGoalItem(
            text = "Treinar 5x na semana",
            onRemoveClick = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
fun AddNewGoalButtonPreview() {
    EvoFitTheme {
        AddNewGoalButton(
            onClick = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
fun PageIndicatorsPreview() {
    EvoFitTheme {
        PageIndicators(
            pageCount = 3,
            selectedPage = 1
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
fun UserInputFieldPreview() {
    EvoFitTheme {
        UserInputField(
            label = "Nome",
            value = "João Silva",
            onValueChange = {}
        )
    }
}


