package com.example.evofit.presentation.ui.feature.onboard.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.evofit.R
import com.example.evofit.data.model.ExerciseModel
import com.example.evofit.data.model.MuscleGroupModel
import com.example.evofit.domain.model.ExerciseCategory
import com.example.evofit.domain.model.GoalSuggestion
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.UserGoal
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import java.util.UUID

enum class GoalFlowStep {
    CHOOSE_CATEGORY,
    STRENGTH_DETAILS,
    CARDIO_DETAILS,
    WEIGHT_DETAILS
}

@Composable
fun NewGoalDialog(
    onDismissRequest: () -> Unit,
    onGoalConfirmed: (UserGoal) -> Unit,
    muscleGroups: List<MuscleGroupModel>,
    getExercises: (String) -> List<ExerciseModel>,
    initialSuggestion: GoalSuggestion? = null
) {
    var currentStep by remember(initialSuggestion) {
        mutableStateOf(
            initialSuggestion?.let { 
                when {
                    it.isWeightGoal -> GoalFlowStep.WEIGHT_DETAILS
                    it.category == ExerciseCategory.STRENGTH -> GoalFlowStep.STRENGTH_DETAILS
                    it.category == ExerciseCategory.CARDIO -> GoalFlowStep.CARDIO_DETAILS
                    else -> GoalFlowStep.CHOOSE_CATEGORY
                }
            } ?: GoalFlowStep.CHOOSE_CATEGORY
        ) 
    }
    
    var selectedMuscleGroup by remember(initialSuggestion) { 
        mutableStateOf(
            initialSuggestion?.muscleGroupId?.let { id -> 
                muscleGroups.find { it.id == id } 
            }
        ) 
    }
    
    var selectedExercise by remember(initialSuggestion, selectedMuscleGroup) { 
        mutableStateOf(
            initialSuggestion?.exerciseId?.let { id ->
                selectedMuscleGroup?.let { group ->
                    getExercises(group.id).find { it.id == id }
                }
            }
        ) 
    }
    
    var weightObjective by remember(initialSuggestion) { mutableStateOf("") }
    
    var selectedCardio by remember(initialSuggestion) { 
        mutableStateOf<ExerciseModel?>(null) 
    }

    LaunchedEffect(initialSuggestion, selectedExercise) {
        if (initialSuggestion?.category == ExerciseCategory.CARDIO) {
            selectedCardio = selectedExercise
        }
    }
    
    var cardioDistance by remember(initialSuggestion) { mutableStateOf("") }
    var cardioTime by remember(initialSuggestion) { mutableStateOf("") }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (initialSuggestion != null) 
                            stringResource(R.string.goal_dialog_title_complete) 
                        else 
                            stringResource(R.string.goal_dialog_title_new),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismissRequest) {
                        Icon(
                            Icons.Default.Close, 
                            contentDescription = stringResource(R.string.goal_dialog_close), 
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedContent(targetState = currentStep, label = "GoalFlow") { step ->
                    when (step) {
                        GoalFlowStep.CHOOSE_CATEGORY -> {
                            val strengthText = stringResource(R.string.goal_category_strength)
                            val resistanceText = stringResource(R.string.goal_category_resistance)
                            CategorySelectionStep(
                                onCategorySelected = { category ->
                                    currentStep = when (category) {
                                        strengthText -> GoalFlowStep.STRENGTH_DETAILS
                                        resistanceText -> GoalFlowStep.CARDIO_DETAILS
                                        else -> GoalFlowStep.WEIGHT_DETAILS
                                    }
                                }
                            )
                        }
                        GoalFlowStep.STRENGTH_DETAILS -> {
                            StrengthFlow(
                                muscleGroups = muscleGroups.filter { it.category == ExerciseCategory.STRENGTH },
                                selectedMuscle = selectedMuscleGroup,
                                onMuscleSelect = {
                                    selectedMuscleGroup = it
                                    selectedExercise = null
                                },
                                exercises = selectedMuscleGroup?.let { getExercises(it.id) }
                                    ?: emptyList(),
                                selectedExercise = selectedExercise,
                                onExerciseSelect = { selectedExercise = it },
                                goalValue = weightObjective,
                                onGoalValueChange = { weightObjective = it },
                                onConfirm = {
                                    selectedExercise?.let {
                                        onGoalConfirmed(
                                            UserGoal.Strength(
                                                id = UUID.randomUUID().toString(),
                                                exerciseName = it.name,
                                                value = weightObjective,
                                                unit = it.unit
                                            )
                                        )
                                    }
                                    onDismissRequest()
                                }
                            )
                        }
                        GoalFlowStep.CARDIO_DETAILS -> {
                            CardioFlow(
                                cardioExercises = muscleGroups.find { it.category == ExerciseCategory.CARDIO }
                                    ?.let { getExercises(it.id) } ?: emptyList(),
                                selectedCardio = selectedCardio ?: selectedExercise,
                                onCardioSelect = { selectedCardio = it },
                                distance = cardioDistance,
                                onDistanceChange = { cardioDistance = it },
                                selectedTime = cardioTime,
                                onTimeSelect = { cardioTime = it },
                                onConfirm = {
                                    val finalCardio = selectedCardio ?: selectedExercise
                                    finalCardio?.let {
                                        onGoalConfirmed(
                                            UserGoal.Cardio(
                                                id = UUID.randomUUID().toString(),
                                                type = it.name,
                                                distance = if (it.unit == MeasurementUnit.DISTANCE) cardioDistance else null,
                                                time = cardioTime
                                            )
                                        )
                                    }
                                    onDismissRequest()
                                }
                            )
                        }
                        GoalFlowStep.WEIGHT_DETAILS -> {
                            WeightFlow(
                                weight = weightObjective,
                                onWeightChange = { weightObjective = it },
                                onConfirm = {
                                    onGoalConfirmed(
                                        UserGoal.Weight(
                                            id = UUID.randomUUID().toString(),
                                            targetWeight = weightObjective
                                        )
                                    )
                                    onDismissRequest()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategorySelectionStep(onCategorySelected: (String) -> Unit) {
    val categories = listOf(
        stringResource(R.string.goal_category_strength),
        stringResource(R.string.goal_category_resistance),
        stringResource(R.string.goal_category_weight_loss),
        stringResource(R.string.goal_category_muscle_gain)
    )
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = stringResource(R.string.goal_dialog_choose_type), 
            color = MaterialTheme.colorScheme.secondary, 
            fontSize = 14.sp
        )
        categories.forEach { category ->
            Button(
                onClick = { onCategorySelected(category) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(category, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Composable
private fun StrengthFlow(
    muscleGroups: List<MuscleGroupModel>,
    selectedMuscle: MuscleGroupModel?,
    onMuscleSelect: (MuscleGroupModel) -> Unit,
    exercises: List<ExerciseModel>,
    selectedExercise: ExerciseModel?,
    onExerciseSelect: (ExerciseModel) -> Unit,
    goalValue: String,
    onGoalValueChange: (String) -> Unit,
    onConfirm: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    
    val filteredExercises = remember(exercises, searchQuery) {
        if (searchQuery.isBlank()) exercises
        else exercises.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(stringResource(R.string.goal_step_muscle_group), color = MaterialTheme.colorScheme.secondary)
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            muscleGroups.forEach { group ->
                SelectionChip(
                    text = group.name,
                    isSelected = selectedMuscle?.id == group.id,
                    onClick = { onMuscleSelect(group) }
                )
            }
        }

        if (selectedMuscle != null) {
            Text(stringResource(R.string.goal_step_exercise), color = MaterialTheme.colorScheme.secondary)

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(stringResource(R.string.goal_search_placeholder), color = MaterialTheme.colorScheme.outline) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.secondary) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filteredExercises.forEach { exercise ->
                    SelectionChip(
                        text = exercise.name,
                        isSelected = selectedExercise?.id == exercise.id,
                        onClick = { onExerciseSelect(exercise) }
                    )
                }
            }
        } else {
            Text(
                text = stringResource(R.string.goal_error_select_muscle), 
                color = MaterialTheme.colorScheme.outlineVariant, 
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        
        val label = when(selectedExercise?.unit) {
            MeasurementUnit.REPS -> stringResource(R.string.goal_step_reps)
            MeasurementUnit.TIME -> stringResource(R.string.goal_step_time)
            else -> stringResource(R.string.goal_step_weight)
        }
        
        val suffixText = when(selectedExercise?.unit) {
            MeasurementUnit.REPS -> stringResource(R.string.goal_unit_reps)
            MeasurementUnit.TIME -> stringResource(R.string.goal_unit_min)
            else -> stringResource(R.string.goal_unit_kg)
        }

        Text(label, color = if (selectedExercise != null) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline)
        
        OutlinedTextField(
            value = goalValue,
            onValueChange = onGoalValueChange,
            enabled = selectedExercise != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            suffix = { Text(suffixText) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface, 
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                disabledTextColor = MaterialTheme.colorScheme.outline,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                disabledBorderColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onConfirm,
            enabled = selectedExercise != null && goalValue.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                Icons.Default.Check, 
                contentDescription = null, 
                tint = if (selectedExercise != null && goalValue.isNotEmpty()) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
private fun CardioFlow(
    cardioExercises: List<ExerciseModel>,
    selectedCardio: ExerciseModel?,
    onCardioSelect: (ExerciseModel) -> Unit,
    distance: String,
    onDistanceChange: (String) -> Unit,
    selectedTime: String,
    onTimeSelect: (String) -> Unit,
    onConfirm: () -> Unit
) {
    val times = listOf("10m", "20m", "30m", "1h")
    val isDistanceRequired = selectedCardio?.unit == MeasurementUnit.DISTANCE

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(stringResource(R.string.goal_step_cardio_select), color = MaterialTheme.colorScheme.secondary)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            cardioExercises.forEach { exercise ->
                SelectionChip(
                    text = exercise.name,
                    isSelected = selectedCardio?.id == exercise.id,
                    onClick = { onCardioSelect(exercise) }
                )
            }
        }

        if (isDistanceRequired) {
            Text(
                text = stringResource(R.string.goal_step_cardio_distance), 
                color = if (selectedCardio != null) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline
            )
            OutlinedTextField(
                value = distance,
                onValueChange = onDistanceChange,
                enabled = selectedCardio != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                suffix = { Text(stringResource(R.string.goal_unit_km)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface, 
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledTextColor = MaterialTheme.colorScheme.outline,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    disabledBorderColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Text(
            text = if (isDistanceRequired) 
                stringResource(R.string.goal_step_cardio_duration_step3) 
            else 
                stringResource(R.string.goal_step_cardio_duration_step2), 
            color = if (selectedCardio != null) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline
        )
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            times.forEach { t ->
                SelectionChip(
                    text = t,
                    isSelected = selectedTime == t,
                    onClick = { if (selectedCardio != null) onTimeSelect(t) }
                )
            }
        }

        val canConfirm = selectedCardio != null && selectedTime.isNotEmpty() && (!isDistanceRequired || distance.isNotEmpty())

        Button(
            onClick = onConfirm,
            enabled = canConfirm,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                Icons.Default.Check,
                contentDescription = null,
                tint = if (canConfirm) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
private fun WeightFlow(
    weight: String,
    onWeightChange: (String) -> Unit,
    onConfirm: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(stringResource(R.string.goal_step_body_weight), color = MaterialTheme.colorScheme.secondary)
        OutlinedTextField(
            value = weight,
            onValueChange = onWeightChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            suffix = { Text(stringResource(R.string.goal_unit_kg)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface, 
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onConfirm,
            enabled = weight.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                Icons.Default.Check,
                contentDescription = null,
                tint = if (weight.isNotEmpty()) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun SelectionChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text, 
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface, 
            fontSize = 14.sp
        )
    }
}
