package com.example.evofit.data.datasource

import com.example.evofit.data.model.ExerciseModel
import com.example.evofit.data.model.MuscleGroupModel
import com.example.evofit.data.model.MuscleGroupType
import com.example.evofit.data.model.MuscleGroupWithExercises
import com.example.evofit.domain.model.ExerciseCategory
import com.example.evofit.domain.model.GoalSuggestion
import com.example.evofit.domain.model.MeasurementUnit

class LocalExerciseDataSource {
    private val groups = listOf(
        MuscleGroupModel("1", "Costas", MuscleGroupType.BACK, ExerciseCategory.STRENGTH),
        MuscleGroupModel("2", "Peito", MuscleGroupType.CHEST, ExerciseCategory.STRENGTH),
        MuscleGroupModel("3", "Pernas", MuscleGroupType.LEGS, ExerciseCategory.STRENGTH),
        MuscleGroupModel("4", "Braços", MuscleGroupType.ARMS, ExerciseCategory.STRENGTH),
        MuscleGroupModel("5", "Ombros", MuscleGroupType.SHOULDERS, ExerciseCategory.STRENGTH),
        MuscleGroupModel("6", "Core", MuscleGroupType.ABS, ExerciseCategory.STRENGTH),
        MuscleGroupModel("7", "Cardio", MuscleGroupType.CARDIO, ExerciseCategory.CARDIO),
        MuscleGroupModel("8", "Gluteo", MuscleGroupType.GLUTES, ExerciseCategory.STRENGTH),
        MuscleGroupModel("9", "Panturrilha", MuscleGroupType.CALVES, ExerciseCategory.STRENGTH),
    )

    private val exercises = listOf(
        // Costas (1)
        ExerciseModel("1", "Puxada Frontal", "1", MeasurementUnit.WEIGHT),
        ExerciseModel("2", "Barra Fixa", "1", MeasurementUnit.REPS),
        ExerciseModel("3", "Remada Curvada", "1", MeasurementUnit.WEIGHT),
        ExerciseModel("4", "Remada Unilateral", "1", MeasurementUnit.WEIGHT),
        ExerciseModel("5", "Remada Sentada", "1", MeasurementUnit.WEIGHT),
        ExerciseModel("6", "Pulldown", "1", MeasurementUnit.WEIGHT),
        ExerciseModel("7", "Remada Cavalinho", "1", MeasurementUnit.WEIGHT),
        ExerciseModel("8", "Pullover na Polia", "1", MeasurementUnit.WEIGHT),
        ExerciseModel("9", "Remada T-Bar", "1", MeasurementUnit.WEIGHT),
        ExerciseModel("10", "Levantamento Terra", "1", MeasurementUnit.WEIGHT),

        // Peito (2)
        ExerciseModel("11", "Supino Reto", "2", MeasurementUnit.WEIGHT),
        ExerciseModel("12", "Supino Inclinado", "2", MeasurementUnit.WEIGHT),
        ExerciseModel("13", "Supino Declinado", "2", MeasurementUnit.WEIGHT),
        ExerciseModel("14", "Crucifixo", "2", MeasurementUnit.WEIGHT),
        ExerciseModel("15", "Crucifixo Inclinado", "2", MeasurementUnit.WEIGHT),
        ExerciseModel("16", "Peck Deck", "2", MeasurementUnit.WEIGHT),
        ExerciseModel("17", "Crossover", "2", MeasurementUnit.WEIGHT),
        ExerciseModel("18", "Flexão de Braço", "2", MeasurementUnit.REPS),
        ExerciseModel("19", "Supino Máquina", "2", MeasurementUnit.WEIGHT),
        ExerciseModel("20", "Chest Press", "2", MeasurementUnit.WEIGHT),

        // Pernas (3)
        ExerciseModel("21", "Agachamento Livre", "3", MeasurementUnit.WEIGHT),
        ExerciseModel("22", "Leg Press", "3", MeasurementUnit.WEIGHT),
        ExerciseModel("23", "Hack Squat", "3", MeasurementUnit.WEIGHT),
        ExerciseModel("24", "Cadeira Extensora", "3", MeasurementUnit.WEIGHT),
        ExerciseModel("25", "Mesa Flexora", "3", MeasurementUnit.WEIGHT),
        ExerciseModel("26", "Cadeira Flexora", "3", MeasurementUnit.WEIGHT),
        ExerciseModel("27", "Afundo", "3", MeasurementUnit.WEIGHT),
        ExerciseModel("28", "Passada", "3", MeasurementUnit.WEIGHT),
        ExerciseModel("29", "Agachamento Búlgaro", "3", MeasurementUnit.WEIGHT),
        ExerciseModel("30", "Stiff", "3", MeasurementUnit.WEIGHT),
        ExerciseModel("31", "Levantamento Terra Romeno", "3", MeasurementUnit.WEIGHT),

        // Braços (4)
        ExerciseModel("32", "Rosca Direta", "4", MeasurementUnit.WEIGHT),
        ExerciseModel("33", "Rosca Alternada", "4", MeasurementUnit.WEIGHT),
        ExerciseModel("34", "Rosca Martelo", "4", MeasurementUnit.WEIGHT),
        ExerciseModel("35", "Rosca Scott", "4", MeasurementUnit.WEIGHT),
        ExerciseModel("36", "Rosca Concentrada", "4", MeasurementUnit.WEIGHT),
        ExerciseModel("37", "Tríceps Pulley", "4", MeasurementUnit.WEIGHT),
        ExerciseModel("38", "Tríceps Francês", "4", MeasurementUnit.WEIGHT),
        ExerciseModel("39", "Tríceps Testa", "4", MeasurementUnit.WEIGHT),
        ExerciseModel("40", "Mergulho Paralelas", "4", MeasurementUnit.REPS),
        ExerciseModel("41", "Rosca Inversa", "4", MeasurementUnit.WEIGHT),
        ExerciseModel("42", "Flexão de Punho", "4", MeasurementUnit.WEIGHT),

        // Ombros (5)
        ExerciseModel("43", "Desenvolvimento Militar", "5", MeasurementUnit.WEIGHT),
        ExerciseModel("44", "Desenvolvimento Halteres", "5", MeasurementUnit.WEIGHT),
        ExerciseModel("45", "Elevação Lateral", "5", MeasurementUnit.WEIGHT),
        ExerciseModel("46", "Elevação Frontal", "5", MeasurementUnit.WEIGHT),
        ExerciseModel("47", "Crucifixo Inverso", "5", MeasurementUnit.WEIGHT),
        ExerciseModel("48", "Face Pull", "5", MeasurementUnit.WEIGHT),
        ExerciseModel("49", "Desenvolvimento Máquina", "5", MeasurementUnit.WEIGHT),
        ExerciseModel("50", "Remada Alta", "5", MeasurementUnit.WEIGHT),
        ExerciseModel("51", "Arnold Press", "5", MeasurementUnit.WEIGHT),

        // Core (6)
        ExerciseModel("52", "Abdominal Tradicional", "6", MeasurementUnit.REPS),
        ExerciseModel("53", "Abdominal Infra", "6", MeasurementUnit.REPS),
        ExerciseModel("54", "Prancha", "6", MeasurementUnit.TIME),
        ExerciseModel("55", "Prancha Lateral", "6", MeasurementUnit.TIME),
        ExerciseModel("56", "Elevação de Pernas", "6", MeasurementUnit.REPS),
        ExerciseModel("57", "Abdominal Bicicleta", "6", MeasurementUnit.REPS),
        ExerciseModel("58", "Abdominal Máquina", "6", MeasurementUnit.WEIGHT),
        ExerciseModel("59", "Crunch na Polia", "6", MeasurementUnit.WEIGHT),
        ExerciseModel("60", "Russian Twist", "6", MeasurementUnit.REPS),
        ExerciseModel("61", "Mountain Climber", "6", MeasurementUnit.TIME),

        // Cardio (7)
        ExerciseModel("62", "Esteira Caminhada", "7", MeasurementUnit.DISTANCE),
        ExerciseModel("63", "Esteira Corrida", "7", MeasurementUnit.DISTANCE),
        ExerciseModel("64", "Bicicleta Ergométrica", "7", MeasurementUnit.DISTANCE),
        ExerciseModel("65", "Elíptico", "7", MeasurementUnit.DISTANCE),
        ExerciseModel("66", "Escada", "7", MeasurementUnit.TIME),
        ExerciseModel("67", "Remo", "7", MeasurementUnit.DISTANCE),
        ExerciseModel("68", "Pular Corda", "7", MeasurementUnit.TIME),
        ExerciseModel("69", "Corrida Externa", "7", MeasurementUnit.DISTANCE),
        ExerciseModel("70", "Caminhada", "7", MeasurementUnit.DISTANCE),
        ExerciseModel("71", "HIIT", "7", MeasurementUnit.TIME),

        // Gluteo (8)
        ExerciseModel("72", "Elevação Pélvica", "8", MeasurementUnit.WEIGHT),
        ExerciseModel("73", "Hip Thrust", "8", MeasurementUnit.WEIGHT),
        ExerciseModel("74", "Coice na Polia", "8", MeasurementUnit.WEIGHT),
        ExerciseModel("75", "Abdução de Quadril", "8", MeasurementUnit.WEIGHT),
        ExerciseModel("76", "Agachamento Sumô", "8", MeasurementUnit.WEIGHT),
        ExerciseModel("77", "Stiff", "8", MeasurementUnit.WEIGHT),
        ExerciseModel("78", "Afundo", "8", MeasurementUnit.WEIGHT),
        ExerciseModel("79", "Passada", "8", MeasurementUnit.WEIGHT),
        ExerciseModel("80", "Glúteo Máquina", "8", MeasurementUnit.WEIGHT),
        ExerciseModel("81", "Ponte de Glúteo", "8", MeasurementUnit.REPS),

        // Panturrilha (9)
        ExerciseModel("82", "Panturrilha em Pé", "9", MeasurementUnit.WEIGHT),
        ExerciseModel("83", "Panturrilha Sentado", "9", MeasurementUnit.WEIGHT),
        ExerciseModel("84", "Panturrilha no Leg Press", "9", MeasurementUnit.WEIGHT),
        ExerciseModel("85", "Panturrilha Smith", "9", MeasurementUnit.WEIGHT),
        ExerciseModel("86", "Panturrilha Unilateral", "9", MeasurementUnit.WEIGHT),
        ExerciseModel("87", "Panturrilha no Hack", "9", MeasurementUnit.WEIGHT),
        ExerciseModel("88", "Elevação de Panturrilha Degrau", "9", MeasurementUnit.REPS),
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

    fun getExercisesByIds(ids: List<String>): List<ExerciseModel> {
        return exercises.filter { it.id in ids }
    }

    fun getSuggestions(): List<GoalSuggestion> = suggestions

    fun getMuscleGroupWithExercises(groupId: String): MuscleGroupWithExercises? {
        val group = groups.find { it.id == groupId } ?: return null
        val list = getExercisesByMuscleGroup(groupId)
        return MuscleGroupWithExercises(group, list)
    }
}
