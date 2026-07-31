package com.example.evofit.presentation.ui.feature.workout.createworkout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.evofit.R
import com.example.evofit.domain.model.MuscleGroupType
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.presentation.mapper.toIcon
import com.example.evofit.presentation.ui.feature.workout.components.configure.AddSetDashedButton
import com.example.evofit.presentation.ui.feature.workout.components.configure.RepsCounterComponent
import com.example.evofit.presentation.ui.feature.workout.components.configure.WeightWheelSelector
import com.example.evofit.presentation.ui.feature.workout.createworkout.state.ExerciseConfigState
import com.example.evofit.presentation.ui.feature.workout.createworkout.state.SetState
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme

@Composable
fun ExerciseSetRow(
    index: Int,
    set: SetState,
    unit: MeasurementUnit,
    onRemoveSet: () -> Unit,
    onUpdateSet: (Double, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(0.8f)
                .height(Dimens.ButtonHeightSecondary)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(Dimens.SpacingMediumSmall)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(Dimens.AuthBadgeSizeDefault) // ~36.dp
                    .background(
                        MaterialTheme.colorScheme.error,
                        RoundedCornerShape(topStart = Dimens.SpacingMediumSmall, bottomStart = Dimens.SpacingMediumSmall)
                    )
                    .clickable { onRemoveSet() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "–",
                    color = MaterialTheme.colorScheme.onError,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.offset(y = (-1).dp)
                )
            }

            Text(
                text = "${index + 1}",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.width(Dimens.SpacingSmall))

        when (unit) {
            MeasurementUnit.WEIGHT -> {
                WeightWheelSelector(
                    modifier = Modifier.weight(1f),
                    initialWeight = set.weight,
                    onWeightSelected = { newWeight ->
                        onUpdateSet(newWeight, set.reps)
                    }
                )

                Spacer(modifier = Modifier.width(Dimens.SpacingSmall))

                RepsCounterComponent(
                    modifier = Modifier.weight(1.2f),
                    value = set.reps,
                    step = 1,
                    onValueChange = { newReps ->
                        onUpdateSet(set.weight, newReps)
                    }
                )
            }
            MeasurementUnit.DISTANCE -> {
                RepsCounterComponent(
                    modifier = Modifier.weight(1.5f),
                    value = set.weight.toInt(),
                    step = 1,
                    onValueChange = { newDistance ->
                        onUpdateSet(newDistance.toDouble(), set.reps)
                    }
                )

                Spacer(modifier = Modifier.width(Dimens.SpacingSmall))

                RepsCounterComponent(
                    modifier = Modifier.weight(1.5f),
                    value = set.reps,
                    step = 5,
                    onValueChange = { newTime ->
                        onUpdateSet(set.weight, newTime)
                    }
                )
            }
            MeasurementUnit.TIME -> {
                RepsCounterComponent(
                    modifier = Modifier.weight(2.2f),
                    value = set.reps,
                    step = 1,
                    onValueChange = { newTime ->
                        onUpdateSet(set.weight, newTime)
                    }
                )
            }
            MeasurementUnit.REPS -> {
                RepsCounterComponent(
                    modifier = Modifier.weight(2.2f),
                    value = set.reps,
                    step = 1,
                    onValueChange = { newReps ->
                        onUpdateSet(set.weight, newReps)
                    }
                )
            }
        }
    }
}

@Composable
fun ExerciseConfigHeader(
    name: String,
    setCount: Int,
    muscleGroupType: MuscleGroupType?,
    unit: MeasurementUnit,
    modifier: Modifier = Modifier
) {
    val muscleGroupIcon = muscleGroupType?.toIcon()

    Column(modifier = modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
        ) {
            Box(
                modifier = Modifier
                    .size(Dimens.MinimumTouchTarget)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (muscleGroupIcon != null) {
                    Icon(
                        imageVector = muscleGroupIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(Dimens.IconSizeDefault)
                    )
                }
            }
            Column {
                Text(
                    text = name,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(R.string.configure_workout_header_sets, setCount),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Spacer(modifier = Modifier.height(Dimens.SpacingLarge))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.configure_workout_col_set),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(0.8f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.width(Dimens.SpacingSmall))

            val (col1Text, col1Weight) = when (unit) {
                MeasurementUnit.WEIGHT -> stringResource(R.string.configure_workout_col_weight) to 1f
                MeasurementUnit.DISTANCE -> stringResource(R.string.configure_workout_col_distance) to 1.5f
                MeasurementUnit.TIME -> stringResource(R.string.configure_workout_col_time) to 2.2f
                MeasurementUnit.REPS -> stringResource(R.string.configure_workout_col_reps_only) to 2.2f
            }

            Text(
                text = col1Text,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(col1Weight),
                textAlign = TextAlign.Center
            )

            if (unit == MeasurementUnit.WEIGHT || unit == MeasurementUnit.DISTANCE) {
                Spacer(modifier = Modifier.width(Dimens.SpacingSmall))

                val (col2Text, col2Weight) = when (unit) {
                    MeasurementUnit.WEIGHT -> stringResource(R.string.configure_workout_col_reps) to 1.2f
                    MeasurementUnit.DISTANCE -> stringResource(R.string.configure_workout_col_time) to 1.5f
                    else -> "" to 0f
                }
                Text(
                    text = col2Text,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(col2Weight),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ExerciseConfigFooter(
    onAddSet: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
        AddSetDashedButton(onClick = onAddSet)
        Spacer(modifier = Modifier.height(Dimens.SpacingExtraLargePlus))
    }
}

@Composable
fun ExerciseConfigContent(
    config: ExerciseConfigState,
    muscleGroupType: MuscleGroupType?,
    onAddSet: (String) -> Unit,
    onUpdateSet: (String, Int, Double, Int) -> Unit,
    onRemoveSet: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.ScreenPaddingHorizontal),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
    ) {
        item {
            ExerciseConfigHeader(
                name = config.name,
                setCount = config.sets.size,
                muscleGroupType = muscleGroupType,
                unit = config.unit
            )
        }

        itemsIndexed(config.sets) { index, item ->
            ExerciseSetRow(
                index = index,
                set = item,
                unit = config.unit,
                onRemoveSet = { onRemoveSet(config.exerciseId, index) },
                onUpdateSet = { weight, reps -> onUpdateSet(config.exerciseId, index, weight, reps) }
            )
        }

        item {
            ExerciseConfigFooter(
                onAddSet = { onAddSet(config.exerciseId) }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun ExerciseSetRowPreview() {
    EvoFitTheme {
        ExerciseSetRow(
            index = 0,
            set = SetState(
                setNumber = 1,
                weight = 60.0,
                reps = 10
            ),
            unit = MeasurementUnit.WEIGHT,
            onRemoveSet = {},
            onUpdateSet = { _: Double, _: Int -> }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun ExerciseConfigContentDistancePreview() {
    EvoFitTheme {
        val mockConfig = remember {
            ExerciseConfigState(
                exerciseId = "1",
                name = "Corrida",
                muscleGroupId = "7",
                unit = MeasurementUnit.DISTANCE,
                sets = listOf(
                    SetState(setNumber = 1, weight = 5.0, reps = 30),
                    SetState(setNumber = 2, weight = 5.0, reps = 25)
                )
            )
        }
        ExerciseConfigContent(
            config = mockConfig,
            muscleGroupType = MuscleGroupType.CARDIO,
            onAddSet = {},
            onUpdateSet = { _, _, _, _ -> },
            onRemoveSet = { _, _ -> }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun ExerciseConfigContentTimePreview() {
    EvoFitTheme {
        val mockConfig = remember {
            ExerciseConfigState(
                exerciseId = "1",
                name = "Prancha",
                muscleGroupId = "6",
                unit = MeasurementUnit.TIME,
                sets = listOf(
                    SetState(setNumber = 1, weight = 0.0, reps = 60),
                    SetState(setNumber = 2, weight = 0.0, reps = 45),
                    SetState(setNumber = 3, weight = 0.0, reps = 30)
                )
            )
        }
        ExerciseConfigContent(
            config = mockConfig,
            muscleGroupType = MuscleGroupType.ABS,
            onAddSet = {},
            onUpdateSet = { _, _, _, _ -> },
            onRemoveSet = { _, _ -> }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun ExerciseConfigContentRepsPreview() {
    EvoFitTheme {
        val mockConfig = remember {
            ExerciseConfigState(
                exerciseId = "1",
                name = "Flexão de Braço",
                muscleGroupId = "2",
                unit = MeasurementUnit.REPS,
                sets = listOf(
                    SetState(setNumber = 1, weight = 0.0, reps = 20),
                    SetState(setNumber = 2, weight = 0.0, reps = 15),
                    SetState(setNumber = 3, weight = 0.0, reps = 12)
                )
            )
        }
        ExerciseConfigContent(
            config = mockConfig,
            muscleGroupType = MuscleGroupType.CHEST,
            onAddSet = {},
            onUpdateSet = { _, _, _, _ -> },
            onRemoveSet = { _, _ -> }
        )
    }
}
