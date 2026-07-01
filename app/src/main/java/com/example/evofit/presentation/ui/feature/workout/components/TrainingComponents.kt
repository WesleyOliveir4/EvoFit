package com.example.evofit.presentation.ui.feature.workout.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.example.evofit.R
import com.example.evofit.presentation.model.ExerciseSelectionUIModel
import com.example.evofit.presentation.model.MuscleGroupItem
import com.example.evofit.presentation.model.WorkoutUIModel
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map


@Composable
fun HeaderSection(
    userName: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = stringResource(R.string.main_workout_greeting, userName),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.main_workout_subtitle),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp
            )
        }

        Box(
            modifier = Modifier
                .size(48.dp)
                .background(MaterialTheme.colorScheme.surface, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun StatCard(
    value: String,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = value,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 12.sp
            )
        }
    }
}

@Stable
class WorkoutDraggableListState(
    private val onMoveState: State<(Int, Int) -> Unit>,
    private val density: Density
) {
    var draggedItemId by mutableStateOf<Int?>(null)
        private set
    var dragOffset by mutableFloatStateOf(0f)
        private set

    fun onDragStart(id: Int) {
        draggedItemId = id
        dragOffset = 0f
    }

    fun onDrag(deltaY: Float, workouts: List<WorkoutUIModel>) {
        dragOffset += deltaY
        val itemHeightPx = with(density) { 104.dp.toPx() }
        val currentIndex = workouts.indexOfFirst { it.id == draggedItemId }

        if (currentIndex != -1) {
            val threshold = itemHeightPx * 0.4f
            if (dragOffset > threshold && currentIndex < workouts.size - 1) {
                onMoveState.value(currentIndex, currentIndex + 1)
                dragOffset -= itemHeightPx
            } else if (dragOffset < -threshold && currentIndex > 0) {
                onMoveState.value(currentIndex, currentIndex - 1)
                dragOffset += itemHeightPx
            }
        }
    }

    fun onDragEnd() {
        draggedItemId = null
        dragOffset = 0f
    }
}

@Composable
fun rememberWorkoutDraggableListState(
    onMove: (Int, Int) -> Unit
): WorkoutDraggableListState {
    val density = LocalDensity.current
    val onMoveState = rememberUpdatedState(onMove)
    return remember(density) {
        WorkoutDraggableListState(onMoveState, density)
    }
}

fun LazyListScope.draggableWorkoutList(
    workouts: List<WorkoutUIModel>,
    dragState: WorkoutDraggableListState
) {
    itemsIndexed(workouts, key = { _, it -> it.id }) { _, workout ->
        val isDragging = dragState.draggedItemId == workout.id
        WorkoutListItem(
            workout = workout,
            isDragging = isDragging,
            dragOffset = dragState.dragOffset,
            modifier = if (isDragging) Modifier.zIndex(10f) else Modifier.animateItem(),
            onDragStart = { dragState.onDragStart(workout.id) },
            onDrag = { deltaY -> dragState.onDrag(deltaY, workouts) },
            onDragEnd = { dragState.onDragEnd() },
            onDragCancel = { dragState.onDragEnd() }
        )
    }
}

@Composable
fun WorkoutListItem(
    workout: WorkoutUIModel,
    modifier: Modifier = Modifier,
    isDragging: Boolean = false,
    dragOffset: Float = 0f,
    onDragStart: () -> Unit = {},
    onDrag: (Float) -> Unit = {},
    onDragEnd: () -> Unit = {},
    onDragCancel: () -> Unit = {}
) {
    val density = LocalDensity.current
    
    val currentOnDragStart by rememberUpdatedState(onDragStart)
    val currentOnDrag by rememberUpdatedState(onDrag)
    val currentOnDragEnd by rememberUpdatedState(onDragEnd)
    val currentOnDragCancel by rememberUpdatedState(onDragCancel)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (isDragging) {
                    Modifier
                        .offset(y = with(density) { dragOffset.toDp() })
                        .scale(1.05f)
                        .shadow(12.dp, RoundedCornerShape(16.dp))
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(16.dp)
                        )
                } else {
                    Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = workout.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = workout.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                val exercisesStr = stringResource(R.string.main_workout_exercise_count, workout.exercises)
                val seriesStr = stringResource(R.string.main_workout_series_count, workout.series)
                Text(
                    text = stringResource(R.string.main_workout_exercise_series_format, exercisesStr, seriesStr),
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 14.sp
                )
            }

            Icon(
                imageVector = Icons.Default.DragIndicator,
                contentDescription = stringResource(R.string.main_workout_drag_handle_desc),
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .size(24.dp)
                    .pointerInput(workout.id) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = { currentOnDragStart() },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                currentOnDrag(dragAmount.y)
                            },
                            onDragEnd = { currentOnDragEnd() },
                            onDragCancel = { currentOnDragCancel() }
                        )
                    }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun HeaderSectionPreview() {
    EvoFitTheme {
        HeaderSection(userName = "Wesley", modifier = Modifier.padding(16.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun StatCardPreview() {
    EvoFitTheme {
        StatCard(
            value = "5",
            label = "Treinos",
            icon = Icons.Default.Favorite,
            modifier = Modifier.padding(16.dp).width(160.dp)
        )
    }
}

@Composable
fun MuscleGroupCard(
    item: MuscleGroupItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.temporaryIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = item.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun MuscleGroupCardPreview() {
    EvoFitTheme {
        MuscleGroupCard(
            item = MuscleGroupItem("peito", "Peito", Icons.Default.Favorite),
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun ExerciseRowItemPreview() {
    EvoFitTheme {
        ExerciseRowItem(
            item = ExerciseSelectionUIModel("1", "Supino Reto"),
            isSelected = true,
            onCheckedChange = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun ExercisePageSegmentedIndicatorPreview() {
    EvoFitTheme {
        ExercisePageSegmentedIndicator(
            totalCount = 5,
            currentIndex = 2,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun WeightWheelSelectorPreview() {
    EvoFitTheme {
        WeightWheelSelector(
            initialWeight = 30.0,
            onWeightSelected = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun RepsCounterComponentPreview() {
    EvoFitTheme {
        RepsCounterComponent(
            value = 12,
            onValueChange = {},
            modifier = Modifier.padding(16.dp).width(120.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun AddSetDashedButtonPreview() {
    EvoFitTheme {
        AddSetDashedButton(
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

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
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable { onCheckedChange(!isSelected) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = isSelected,
                    enter = expandIn(expandFrom = Alignment.Center),
                    exit = shrinkOut(shrinkTowards = Alignment.Center)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                }
                if (!isSelected) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(MaterialTheme.colorScheme.surface, CircleShape)
                    )
                }
            }

            Text(
                text = item.name,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
fun ExercisePageSegmentedIndicator(
    totalCount: Int,
    currentIndex: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        for (i in 0 until totalCount) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .background(
                        color = if (i <= currentIndex) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        }
    }
}

@Composable
fun WeightWheelSelector(
    modifier: Modifier = Modifier,
    initialWeight: Double,
    onWeightSelected: (Double) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    CompactWeightDisplay(
        weight = initialWeight,
        modifier = modifier
            .fillMaxWidth()
            .clickable { showDialog = true }
    )

    if (showDialog) {
        WeightPickerDialog(
            initialWeight = initialWeight,
            onConfirm = { newWeight ->
                onWeightSelected(newWeight)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun CompactWeightDisplay(
    weight: Double,
    modifier: Modifier = Modifier
) {
    val weightStr = if (weight % 1 == 0.0) "${weight.toInt()}" else "$weight"
    val prevWeight = weight - 2.5
    val nextWeight = weight + 2.5
    val prevStr = if (prevWeight >= 0) (if (prevWeight % 1 == 0.0) "${prevWeight.toInt()}" else "$prevWeight") else ""
    val nextStr = if (nextWeight % 1 == 0.0) "${nextWeight.toInt()}" else "$nextWeight"

    Box(
        modifier = modifier
            .widthIn(max = 240.dp)
            .fillMaxWidth()
            .height(48.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (prevStr.isNotEmpty()) {
            Text(
                text = prevStr,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp)
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        layout(placeable.width, placeable.height) {
                            placeable.placeRelative(-(placeable.width * 0.6f).toInt(), 0)
                        }
                    }
            )
        }

        Box(
            modifier = Modifier
                .width(64.dp)
                .height(36.dp)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = weightStr,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Text(
            text = nextStr,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp)
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(placeable.width, placeable.height) {
                        placeable.placeRelative((placeable.width * 0.6f).toInt(), 0)
                    }
                }
        )
    }
}

@Composable
fun WeightPickerDialog(
    initialWeight: Double,
    onConfirm: (Double) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedWeight by remember { mutableStateOf(initialWeight) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.configure_workout_col_weight),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                WeightWheel(
                    initialWeight = initialWeight,
                    onWeightSelected = { selectedWeight = it },
                    isExpanded = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onConfirm(selectedWeight) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Confirmar",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

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
                items(weights.size) { index ->
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
                items(weights.size) { index ->
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

@Composable
fun RepsCounterComponent(
    modifier: Modifier = Modifier,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "–",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 16.sp,
            modifier = Modifier
                .weight(1f)
                .clickable { if (value > 0) onValueChange(value - 1) },
            textAlign = TextAlign.Center
        )
        Text(
            text = "$value",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Text(
            text = "+",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 16.sp,
            modifier = Modifier
                .weight(1f)
                .clickable { onValueChange(value + 1) },
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AddSetDashedButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val strokeColor = MaterialTheme.colorScheme.outlineVariant
    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRoundRect(
                color = strokeColor,
                style = stroke,
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx(), 12.dp.toPx())
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = stringResource(R.string.configure_workout_add_set),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
