package com.example.evofit.presentation.ui.feature.workout.components.configure

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

import androidx.compose.ui.tooling.preview.Preview
import com.example.evofit.presentation.ui.theme.EvoFitTheme

/**
 * Seletor giratório de peso (estilo "wheel picker"), usado dentro de [WeightPickerDialog].
 * Pode ser exibido em modo compacto (linha horizontal) ou expandido (coluna vertical).
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeightWheel(
    modifier: Modifier = Modifier,
    initialWeight: Double,
    onWeightSelected: (Double) -> Unit,
    isExpanded: Boolean = false
) {
    val weights = remember { generateSequence(0.0) { it + 2.5 }.takeWhile { it <= 250 }.toList() }
    val startIndex = remember(initialWeight) {
        val index = weights.indexOfFirst { it >= initialWeight }
        if (index != -1) index else 0
    }

    val listState = rememberLazyListState()
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    LaunchedEffect(startIndex) {
        listState.scrollToItem(startIndex)
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { weights.getOrNull(it) ?: initialWeight }
            .distinctUntilChanged()
            .collect { onWeightSelected(it) }
    }

    Box(
        modifier = modifier
            .height(if (isExpanded) 200.dp else 48.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (isExpanded) {
            LazyColumn(
                state = listState,
                flingBehavior = flingBehavior,
                contentPadding = PaddingValues(vertical = 88.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    count = weights.size,
                    key = { index -> weights[index] }
                ) { index ->
                    val weight = weights[index]
                    val isWhole = weight % 1 == 0.0
                    Text(
                        text = if (isWhole) "${weight.toInt()} kg" else "$weight kg",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else {
            LazyRow(
                state = listState,
                flingBehavior = flingBehavior,
                contentPadding = PaddingValues(horizontal = 36.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = false
            ) {
                items(
                    count = weights.size,
                    key = { index -> weights[index] }
                ) { index ->
                    val weight = weights[index]
                    val isWhole = weight % 1 == 0.0
                    Text(
                        text = if (isWhole) "${weight.toInt()}" else "$weight",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Box(
            modifier = if (isExpanded) {
                Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 20.dp)
                    .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            } else {
                Modifier
                    .width(42.dp)
                    .fillMaxHeight()
                    .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun WeightWheelPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            WeightWheel(
                initialWeight = 60.0,
                onWeightSelected = {}
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun WeightWheelExpandedPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            WeightWheel(
                initialWeight = 60.0,
                onWeightSelected = {},
                isExpanded = true
            )
        }
    }
}
