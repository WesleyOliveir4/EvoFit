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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.evofit.data.model.ExerciseModel
import com.example.evofit.data.model.MuscleGroupModel
import com.example.evofit.domain.model.ExerciseCategory
import com.example.evofit.domain.model.GoalSuggestion
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.model.UserGoal
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
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
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
                        text = if (initialSuggestion != null) "Completar Meta" else "Nova Meta",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismissRequest) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar", tint = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedContent(targetState = currentStep, label = "GoalFlow") { step ->
                    when (step) {
                        GoalFlowStep.CHOOSE_CATEGORY -> {
                            CategorySelectionStep(
                                onCategorySelected = { category ->
                                    currentStep = when (category) {
                                        "Aumentar força" -> GoalFlowStep.STRENGTH_DETAILS
                                        "Melhorar resistência" -> GoalFlowStep.CARDIO_DETAILS
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
    val categories = listOf("Aumentar força", "Melhorar resistência", "Perder peso", "Ganhar massa")
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Escolha o tipo da sua meta:", color = Color.Gray, fontSize = 14.sp)
        categories.forEach { category ->
            Button(
                onClick = { onCategorySelected(category) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2E)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(category, color = Color.White)
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
        Text("1. Qual grupo muscular?", color = Color.Gray)
        
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
            Text("2. Qual exercício?", color = Color.Gray)

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar exercício...", color = Color.DarkGray) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF2C2C2E),
                    unfocusedContainerColor = Color(0xFF2C2C2E),
                    focusedBorderColor = Color(0xFF67D14E)
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
            Text("Selecione um grupo muscular primeiro", color = Color(0xFF444444), fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))
        
        val label = when(selectedExercise?.unit) {
            MeasurementUnit.REPS -> "3. Meta de repetições:"
            MeasurementUnit.TIME -> "3. Meta de tempo (min):"
            else -> "3. Meta de quilos (1RM):"
        }
        
        val suffixText = when(selectedExercise?.unit) {
            MeasurementUnit.REPS -> "reps"
            MeasurementUnit.TIME -> "min"
            else -> "kg"
        }

        Text(label, color = if (selectedExercise != null) Color.Gray else Color.DarkGray)
        
        OutlinedTextField(
            value = goalValue,
            onValueChange = onGoalValueChange,
            enabled = selectedExercise != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            suffix = { Text(suffixText) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White, 
                unfocusedTextColor = Color.White,
                disabledTextColor = Color.DarkGray,
                focusedBorderColor = Color(0xFF67D14E),
                disabledBorderColor = Color(0xFF2C2C2E)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onConfirm,
            enabled = selectedExercise != null && goalValue.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF67D14E),
                disabledContainerColor = Color(0xFF2C2C2E)
            ),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                Icons.Default.Check, 
                contentDescription = null, 
                tint = if (selectedExercise != null && goalValue.isNotEmpty()) Color.Black else Color.Gray
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
        Text("1. Selecione o Cardio:", color = Color.Gray)
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
            Text("2. Distância aproximada:", color = if (selectedCardio != null) Color.Gray else Color.DarkGray)
            OutlinedTextField(
                value = distance,
                onValueChange = onDistanceChange,
                enabled = selectedCardio != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                suffix = { Text("km") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White, 
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.DarkGray,
                    focusedBorderColor = Color(0xFF67D14E),
                    disabledBorderColor = Color(0xFF2C2C2E)
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Text(
            if (isDistanceRequired) "3. Duração:" else "2. Duração:", 
            color = if (selectedCardio != null) Color.Gray else Color.DarkGray
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
                containerColor = Color(0xFF67D14E),
                disabledContainerColor = Color(0xFF2C2C2E)
            ),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                Icons.Default.Check,
                contentDescription = null,
                tint = if (canConfirm) Color.Black else Color.Gray
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
        Text("Meta de peso corporal:", color = Color.Gray)
        OutlinedTextField(
            value = weight,
            onValueChange = onWeightChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            suffix = { Text("kg") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White, 
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFF67D14E)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onConfirm,
            enabled = weight.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF67D14E),
                disabledContainerColor = Color(0xFF2C2C2E)
            ),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                Icons.Default.Check,
                contentDescription = null,
                tint = if (weight.isNotEmpty()) Color.Black else Color.Gray
            )
        }
    }
}

@Composable
fun SelectionChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(
                color = if (isSelected) Color(0xFF67D14E) else Color(0xFF2C2C2E),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = text, color = if (isSelected) Color.Black else Color.White, fontSize = 14.sp)
    }
}
