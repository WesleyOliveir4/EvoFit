package com.example.evofit.ui.feature.onboard.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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

enum class GoalFlowStep {
    CHOOSE_CATEGORY,
    STRENGTH_DETAILS,
    CARDIO_DETAILS,
    WEIGHT_DETAILS
}

@Composable
fun NewGoalDialog(
    onDismissRequest: () -> Unit,
    onGoalConfirmed: (String) -> Unit
) {
    var currentStep by remember { mutableStateOf(GoalFlowStep.CHOOSE_CATEGORY) }
    
    var selectedMuscleGroup by remember { mutableStateOf("") }
    var weightObjective by remember { mutableStateOf("") }
    
    var selectedCardio by remember { mutableStateOf("") }
    var cardioDistance by remember { mutableStateOf("") }
    var cardioTime by remember { mutableStateOf("20m") }

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
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nova Meta",
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
                                selectedMuscle = selectedMuscleGroup,
                                onMuscleSelect = { selectedMuscleGroup = it },
                                weight = weightObjective,
                                onWeightChange = { weightObjective = it },
                                onConfirm = {
                                    onGoalConfirmed("Força: $selectedMuscleGroup - ${weightObjective}kg")
                                    onDismissRequest()
                                }
                            )
                        }
                        GoalFlowStep.CARDIO_DETAILS -> {
                            CardioFlow(
                                selectedCardio = selectedCardio,
                                onCardioSelect = { selectedCardio = it },
                                distance = cardioDistance,
                                onDistanceChange = { cardioDistance = it },
                                selectedTime = cardioTime,
                                onTimeSelect = { cardioTime = it },
                                onConfirm = {
                                    onGoalConfirmed("$selectedCardio: ${cardioDistance}km em $cardioTime")
                                    onDismissRequest()
                                }
                            )
                        }
                        GoalFlowStep.WEIGHT_DETAILS -> {
                            WeightFlow(
                                weight = weightObjective,
                                onWeightChange = { weightObjective = it },
                                onConfirm = {
                                    onGoalConfirmed("Meta de Peso: ${weightObjective}kg")
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
    selectedMuscle: String,
    onMuscleSelect: (String) -> Unit,
    weight: String,
    onWeightChange: (String) -> Unit,
    onConfirm: () -> Unit
) {
    val muscles = listOf("Costas", "Peito", "Braços", "Pernas")
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Qual grupo muscular?", color = Color.Gray)
        
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            muscles.forEach { m ->
                SelectionChip(text = m, isSelected = selectedMuscle == m, onClick = { onMuscleSelect(m) })
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text("Meta de quilos (1RM):", color = Color.Gray)
        
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
            enabled = selectedMuscle.isNotEmpty() && weight.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF67D14E)),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black)
        }
    }
}

@Composable
private fun CardioFlow(
    selectedCardio: String,
    onCardioSelect: (String) -> Unit,
    distance: String,
    onDistanceChange: (String) -> Unit,
    selectedTime: String,
    onTimeSelect: (String) -> Unit,
    onConfirm: () -> Unit
) {
    val cardios = listOf("Esteira", "Bicicleta", "Escada")
    val times = listOf("10m", "20m", "30m", "1h")

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Selecione o Cardio:", color = Color.Gray)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            cardios.forEach { c ->
                SelectionChip(text = c, isSelected = selectedCardio == c, onClick = { onCardioSelect(c) })
            }
        }

        Text("Distância aproximada:", color = Color.Gray)
        OutlinedTextField(
            value = distance,
            onValueChange = onDistanceChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            suffix = { Text("km") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White, 
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFF67D14E)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Text("Duração:", color = Color.Gray)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            times.forEach { t ->
                SelectionChip(text = t, isSelected = selectedTime == t, onClick = { onTimeSelect(t) })
            }
        }

        Button(
            onClick = onConfirm,
            enabled = selectedCardio.isNotEmpty() && distance.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF67D14E)),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Adicionar Cardio", color = Color.Black, fontWeight = FontWeight.Bold)
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
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF67D14E)),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Confirmar Meta", color = Color.Black, fontWeight = FontWeight.Bold)
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
