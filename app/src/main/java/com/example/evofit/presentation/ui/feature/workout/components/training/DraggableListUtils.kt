package com.example.evofit.presentation.ui.feature.workout.components.training

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.evofit.presentation.model.WorkoutUIModel

@Stable
class DraggableListState(
    private val onMoveState: State<(Int, Int) -> Unit>,
    private val density: Density,
    private val itemHeight: Dp = 104.dp
) {
    var draggedItemId by mutableStateOf<String?>(null)
        private set
    var dragOffset by mutableFloatStateOf(0f)
        private set

    fun onDragStart(id: String) {
        draggedItemId = id
        dragOffset = 0f
    }

    fun <T> onDrag(deltaY: Float, items: List<T>, idSelector: (T) -> String) {
        val currentIndex = items.indexOfFirst { idSelector(it) == draggedItemId }
        if (currentIndex == -1) return

        val totalItemHeightPx = with(density) { itemHeight.toPx() }
        val newOffset = dragOffset + deltaY
        val threshold = totalItemHeightPx * 0.5f

        when {
            newOffset > threshold && currentIndex < items.lastIndex -> {
                onMoveState.value(currentIndex, currentIndex + 1)
                dragOffset = newOffset - totalItemHeightPx
            }
            newOffset < -threshold && currentIndex > 0 -> {
                onMoveState.value(currentIndex, currentIndex - 1)
                dragOffset = newOffset + totalItemHeightPx
            }
            else -> {
                dragOffset = when {
                    currentIndex == 0 -> newOffset.coerceAtLeast(-totalItemHeightPx * 0.2f)
                    currentIndex == items.lastIndex -> newOffset.coerceAtMost(totalItemHeightPx * 0.2f)
                    else -> newOffset
                }
            }
        }
    }

    fun onDragEnd() {
        draggedItemId = null
        dragOffset = 0f
    }
}

@Composable
fun rememberDraggableListState(
    itemHeight: Dp = 104.dp,
    onMove: (Int, Int) -> Unit
): DraggableListState {
    val density = LocalDensity.current
    val onMoveState = rememberUpdatedState(onMove)
    return remember(density, itemHeight) {
        DraggableListState(onMoveState, density, itemHeight)
    }
}

fun LazyListScope.draggableWorkoutList(
    workouts: List<WorkoutUIModel>,
    dragState: DraggableListState,
    onWorkoutClick: (WorkoutUIModel) -> Unit
) {
    itemsIndexed(workouts, key = { _, it -> it.id }) { _, workout ->
        val isDragging = dragState.draggedItemId == workout.id
        WorkoutListItem(
            workout = workout,
            isDragging = isDragging,
            dragOffset = { if (isDragging) dragState.dragOffset else 0f },
            modifier = Modifier
                .zIndex(if (isDragging) 10f else 1f)
                .then(if (isDragging) Modifier else Modifier.animateItem(
                    fadeInSpec = null,
                    fadeOutSpec = null
                )),
            onDragStart = { dragState.onDragStart(workout.id) },
            onDrag = { deltaY -> dragState.onDrag(deltaY, workouts, { it.id }) },
            onDragEnd = dragState::onDragEnd,
            onDragCancel = dragState::onDragEnd,
            onClick = { onWorkoutClick(workout) }
        )
    }
}
