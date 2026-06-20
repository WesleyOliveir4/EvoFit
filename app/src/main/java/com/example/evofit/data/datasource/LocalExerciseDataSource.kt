package com.example.evofit.data.datasource

import com.example.evofit.data.model.ExerciseModel
import com.example.evofit.data.model.MuscleGroupModel
import com.example.evofit.data.model.MuscleGroupWithExercises
import com.example.evofit.domain.model.ExerciseCategory
import com.example.evofit.domain.model.GoalSuggestion

class LocalExerciseDataSource {
    private val groups = listOf(
        MuscleGroupModel("1", "Costas", ExerciseCategory.STRENGTH),
        MuscleGroupModel("2", "Peito", ExerciseCategory.STRENGTH),
        MuscleGroupModel("3", "Pernas", ExerciseCategory.STRENGTH),
        MuscleGroupModel("4", "Braços", ExerciseCategory.STRENGTH),
        MuscleGroupModel("5", "Ombros", ExerciseCategory.STRENGTH),
        MuscleGroupModel("6", "Core", ExerciseCategory.STRENGTH),
        MuscleGroupModel("7", "Cardio", ExerciseCategory.CARDIO),
        MuscleGroupModel("8", "Gluteo", ExerciseCategory.STRENGTH),
    )

    private val exercises = listOf(
        //Costas
        ExerciseModel("1", "Puxada Alta", "1"),
        ExerciseModel("2", "Remada Baixa", "1"),
        ExerciseModel("15", "Remada Curvada", "1"),
        ExerciseModel("16", "Levantamento Terra", "1"),
        ExerciseModel("17", "Barra Fixa", "1"),
        //Peito
        ExerciseModel("3", "Supino Reto", "2"),
        ExerciseModel("4", "Supino Inclinado", "2"),
        ExerciseModel("18", "Crucifixo", "2"),
        ExerciseModel("19", "Flexão de Braços", "2"),
        ExerciseModel("20", "Crossover", "2"),
        //Pernas
        ExerciseModel("5", "Agachamento", "3"),
        ExerciseModel("6", "Leg Press", "3"),
        ExerciseModel("21", "Extensora", "3"),
        ExerciseModel("22", "Flexora", "3"),
        //Braços
        ExerciseModel("7", "Rosca Direta", "4"),
        ExerciseModel("8", "Tríceps Pulley", "4"),
        ExerciseModel("24", "Rosca Martelo", "4"),
        ExerciseModel("25", "Tríceps Testa", "4"),
        ExerciseModel("26", "Rosca Concentrada", "4"),
        //Ombros
        ExerciseModel("9", "Desenvolvimento", "5"),
        ExerciseModel("27", "Elevação Lateral", "5"),
        ExerciseModel("28", "Elevação Frontal", "5"),
        ExerciseModel("29", "Encolhimento", "5"),
        //Core
        ExerciseModel("10", "Prancha", "6"),
        ExerciseModel("11", "Abdominal", "6"),
        ExerciseModel("30", "Abdominal Infra", "6"),
        ExerciseModel("31", "Roda Abdominal", "6"),
        //Cardio
        ExerciseModel("12", "Esteira", "7"),
        ExerciseModel("13", "Bicicleta", "7"),
        ExerciseModel("14", "Elíptico", "7"),
        ExerciseModel("32", "Corda", "7"),
        ExerciseModel("33", "Escada", "7"),
        //Gluteo
        ExerciseModel("23", "Afundo", "8"),
        ExerciseModel("23", "Afundo", "8"),
    )

    private val suggestions = listOf(
        GoalSuggestion("1", "Perder peso", isWeightGoal = true),
        GoalSuggestion("2", "Ganhar massa", isWeightGoal = true),
        GoalSuggestion("3", "Aumentar força", ExerciseCategory.STRENGTH, muscleGroupId = "3"),
        GoalSuggestion("4", "Melhorar pace", ExerciseCategory.CARDIO, muscleGroupId = "7"),
        GoalSuggestion("5", "Core blindado", ExerciseCategory.STRENGTH, muscleGroupId = "6"),
        GoalSuggestion("6", "Peito de aço", ExerciseCategory.STRENGTH, muscleGroupId = "2")
    )

    fun getAllMuscleGroups(): List<MuscleGroupModel> = groups

    fun getExercisesByMuscleGroup(groupId: String): List<ExerciseModel> {
        return exercises.filter { it.muscleGroupId == groupId }
    }

    fun getSuggestions(): List<GoalSuggestion> = suggestions

    fun getMuscleGroupWithExercises(groupId: String): MuscleGroupWithExercises? {
        val group = groups.find { it.id == groupId } ?: return null
        val list = getExercisesByMuscleGroup(groupId)
        return MuscleGroupWithExercises(group, list)
    }
}
