package com.guaracode.evofit.presentation.ui.feature.workout.createworkout.components

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.guaracode.evofit.domain.model.MuscleGroupType
import com.guaracode.evofit.domain.model.MeasurementUnit
import com.guaracode.evofit.presentation.mapper.toImageRes
import com.guaracode.evofit.presentation.ui.feature.workout.components.configure.AddSetDashedButton
import com.guaracode.evofit.presentation.ui.feature.workout.components.configure.RepsCounterComponent
import com.guaracode.evofit.presentation.ui.feature.workout.components.configure.WeightWheelSelector
import com.guaracode.evofit.presentation.ui.feature.workout.createworkout.state.ExerciseConfigState
import com.guaracode.evofit.presentation.ui.feature.workout.createworkout.state.SetState
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import org.jetbrains.compose.resources.painterResource

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
                    .width(Dimens.AuthBadgeSizeDefault)
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
    val muscleGroupImageRes = muscleGroupType?.toImageRes()

    Column(modifier = modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
        ) {
            Box(
                modifier = Modifier
                    .size(Dimens.FabSizeSmall)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (muscleGroupImageRes != null) {
                    Icon(
                        painter = painterResource(muscleGroupImageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(Dimens.SpacingExtraSmall)
                            .fillMaxSize(),
                        tint = MaterialTheme.colorScheme.primary
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
                    text = "$setCount séries",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Spacer(modifier = Modifier.height(Dimens.SpacingLarge))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "SÉRIE",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(0.8f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.width(Dimens.SpacingSmall))

            val (col1Text, col1Weight) = when (unit) {
                MeasurementUnit.WEIGHT -> "PESO (KG)" to 1f
                MeasurementUnit.DISTANCE -> "DISTÂNCIA (KM)" to 1.5f
                MeasurementUnit.TIME -> "TEMPO (MIN)" to 2.2f
                MeasurementUnit.REPS -> "REPETIÇÕES" to 2.2f
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
                    MeasurementUnit.WEIGHT -> "REPETIÇÕES" to 1.2f
                    MeasurementUnit.DISTANCE -> "TEMPO (MIN)" to 1.5f
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
                muscleGroupType = config.muscleGroupType,
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

@Preview
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
