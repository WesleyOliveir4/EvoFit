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
        MuscleGroupModel("4", "Biceps", MuscleGroupType.BICEPS, ExerciseCategory.STRENGTH),
        MuscleGroupModel("5", "Triceps", MuscleGroupType.TRICEPS, ExerciseCategory.STRENGTH),
        MuscleGroupModel("6", "Antebraço", MuscleGroupType.FOREARMS, ExerciseCategory.STRENGTH),
        MuscleGroupModel("7", "Ombro", MuscleGroupType.SHOULDERS, ExerciseCategory.STRENGTH),
        MuscleGroupModel("8", "Core", MuscleGroupType.ABS, ExerciseCategory.STRENGTH),
        MuscleGroupModel("9", "Cardio", MuscleGroupType.CARDIO, ExerciseCategory.CARDIO),
        MuscleGroupModel("10", "Gluteo", MuscleGroupType.GLUTES, ExerciseCategory.STRENGTH),
        MuscleGroupModel("11", "Panturrilha", MuscleGroupType.CALVES, ExerciseCategory.STRENGTH),
    )

    private val exercises = listOf(
        // Back (1)
        ExerciseModel("1", "Puxada Frontal", "1", MeasurementUnit.WEIGHT, 1),
        ExerciseModel("2", "Barra Fixa", "1", MeasurementUnit.REPS, 2),
        ExerciseModel("3", "Remada Curvada", "1", MeasurementUnit.WEIGHT, 3),
        ExerciseModel("4", "Remada Unilateral", "1", MeasurementUnit.WEIGHT, 4),
        ExerciseModel("5", "Remada Sentada", "1", MeasurementUnit.WEIGHT, 5),
        ExerciseModel("6", "Pulldown", "1", MeasurementUnit.WEIGHT, 6),
        ExerciseModel("7", "Remada Cavalinho", "1", MeasurementUnit.WEIGHT, 7),
        ExerciseModel("8", "Pullover na Polia", "1", MeasurementUnit.WEIGHT, 8),
        ExerciseModel("9", "Remada T-Bar", "1", MeasurementUnit.WEIGHT, 9),
        ExerciseModel("10", "Levantamento Terra", "1", MeasurementUnit.WEIGHT, 10),
        ExerciseModel("92", "Remada Articulada", "1", MeasurementUnit.WEIGHT, 11),
        ExerciseModel("93", "Remada baixa Articulada", "1", MeasurementUnit.WEIGHT, 12),
        ExerciseModel("94", "Puxada com Triângulo", "1", MeasurementUnit.WEIGHT, 13),

        // Chest (2)
        ExerciseModel("11", "Supino Reto", "2", MeasurementUnit.WEIGHT, 1),
        ExerciseModel("12", "Supino Inclinado", "2", MeasurementUnit.WEIGHT, 2),
        ExerciseModel("13", "Supino Declinado", "2", MeasurementUnit.WEIGHT, 3),
        ExerciseModel("14", "Crucifixo", "2", MeasurementUnit.WEIGHT, 4),
        ExerciseModel("15", "Crucifixo Inclinado", "2", MeasurementUnit.WEIGHT, 5),
        ExerciseModel("16", "Peck Deck", "2", MeasurementUnit.WEIGHT, 6),
        ExerciseModel("17", "Crossover", "2", MeasurementUnit.WEIGHT, 7),
        ExerciseModel("18", "Flexão de Braço", "2", MeasurementUnit.REPS, 8),
        ExerciseModel("19", "Supino Máquina", "2", MeasurementUnit.WEIGHT, 9),
        ExerciseModel("20", "Chest Press", "2", MeasurementUnit.WEIGHT, 10, false),
        ExerciseModel("95", "Supino com Halteres", "2", MeasurementUnit.WEIGHT, 11),
        ExerciseModel("96", "Crossover Inferior", "2", MeasurementUnit.WEIGHT, 12),

        // Legs (3)
        ExerciseModel("21", "Agachamento Livre", "3", MeasurementUnit.WEIGHT, 1),
        ExerciseModel("22", "Leg Press", "3", MeasurementUnit.WEIGHT, 2),
        ExerciseModel("23", "Hack Squat", "3", MeasurementUnit.WEIGHT, 3),
        ExerciseModel("24", "Cadeira Extensora", "3", MeasurementUnit.WEIGHT, 4),
        ExerciseModel("25", "Mesa Flexora", "3", MeasurementUnit.WEIGHT, 5),
        ExerciseModel("26", "Cadeira Flexora", "3", MeasurementUnit.WEIGHT, 6),
        ExerciseModel("27", "Afundo", "3", MeasurementUnit.WEIGHT, 7),
        ExerciseModel("28", "Passada", "3", MeasurementUnit.WEIGHT, 8),
        ExerciseModel("29", "Agachamento Búlgaro", "3", MeasurementUnit.WEIGHT, 9),
        ExerciseModel("30", "Stiff", "3", MeasurementUnit.WEIGHT, 10),
        ExerciseModel("31", "Levantamento Terra Romeno", "3", MeasurementUnit.WEIGHT, 11),
        ExerciseModel("97", "Cadeira Abdutora/Adutora", "3", MeasurementUnit.WEIGHT, 12),
        ExerciseModel("98", "Flexão de joelhos", "3", MeasurementUnit.WEIGHT, 13),
        ExerciseModel("99", "Sumo maquina", "3", MeasurementUnit.WEIGHT, 14),
        ExerciseModel("100", "Sumo halter", "3", MeasurementUnit.WEIGHT, 15),

        // Biceps (4)
        ExerciseModel("32", "Rosca Direta", "4", MeasurementUnit.WEIGHT, 1),
        ExerciseModel("33", "Rosca Direta Pulley", "4", MeasurementUnit.WEIGHT, 2),
        ExerciseModel("34", "Rosca Alternada", "4", MeasurementUnit.WEIGHT, 3),
        ExerciseModel("35", "Rosca Martelo", "4", MeasurementUnit.WEIGHT, 4),
        ExerciseModel("36", "Rosca Martelo Pulley", "4", MeasurementUnit.WEIGHT, 5),
        ExerciseModel("37", "Rosca Scott", "4", MeasurementUnit.WEIGHT, 6),
        ExerciseModel("38", "Rosca Concentrada", "4", MeasurementUnit.WEIGHT, 7),
        ExerciseModel("101", "Rosca Inclinada com Halteres", "4", MeasurementUnit.WEIGHT, 8),

        // Triceps (5)
        ExerciseModel("39", "Tríceps Pulley", "5", MeasurementUnit.WEIGHT, 1),
        ExerciseModel("40", "Triceps Pulley Corda", "5", MeasurementUnit.WEIGHT, 2),
        ExerciseModel("41", "Tríceps Francês", "5", MeasurementUnit.WEIGHT, 3),
        ExerciseModel("42", "Tríceps Testa", "5", MeasurementUnit.WEIGHT, 4),
        ExerciseModel("43", "Mergulho Paralelas", "5", MeasurementUnit.REPS, 5),
        ExerciseModel("102", "Coice na Polia", "5", MeasurementUnit.WEIGHT, 6),
        ExerciseModel("103", "Tríceps Banco", "5", MeasurementUnit.REPS, 7),
        ExerciseModel("104", "Extensão Overhead com Halter", "5", MeasurementUnit.WEIGHT, 8),

        // Forearms (6)
        ExerciseModel("44", "Rosca Inversa", "6", MeasurementUnit.WEIGHT, 1),
        ExerciseModel("45", "Flexão de Punho", "6", MeasurementUnit.WEIGHT, 2),

        // Shoulders (7)
        ExerciseModel("46", "Desenvolvimento Militar", "7", MeasurementUnit.WEIGHT, 1),
        ExerciseModel("47", "Desenvolvimento Halteres", "7", MeasurementUnit.WEIGHT, 2),
        ExerciseModel("48", "Elevação Lateral", "7", MeasurementUnit.WEIGHT, 3),
        ExerciseModel("49", "Elevação Frontal", "7", MeasurementUnit.WEIGHT, 4),
        ExerciseModel("50", "Crucifixo Inverso", "7", MeasurementUnit.WEIGHT, 5),
        ExerciseModel("51", "Face Pull", "7", MeasurementUnit.WEIGHT, 6),
        ExerciseModel("52", "Desenvolvimento Máquina", "7", MeasurementUnit.WEIGHT, 7),
        ExerciseModel("53", "Remada Alta", "7", MeasurementUnit.WEIGHT, 8),
        ExerciseModel("54", "Arnold Press", "7", MeasurementUnit.WEIGHT, 9),

        // Core (8)
        ExerciseModel("55", "Abdominal Tradicional", "8", MeasurementUnit.REPS, 1),
        ExerciseModel("56", "Abdominal Infra", "8", MeasurementUnit.REPS, 2),
        ExerciseModel("57", "Prancha", "8", MeasurementUnit.TIME, 3),
        ExerciseModel("58", "Prancha Lateral", "8", MeasurementUnit.TIME, 4),
        ExerciseModel("59", "Elevação de Pernas", "8", MeasurementUnit.REPS, 5),
        ExerciseModel("60", "Abdominal Bicicleta", "8", MeasurementUnit.REPS, 6),
        ExerciseModel("61", "Abdominal Máquina", "8", MeasurementUnit.WEIGHT, 7),
        ExerciseModel("62", "Crunch na Polia", "8", MeasurementUnit.WEIGHT, 8),
        ExerciseModel("63", "Russian Twist", "8", MeasurementUnit.REPS, 9),
        ExerciseModel("64", "Mountain Climber", "8", MeasurementUnit.TIME, 10),

        // Cardio (9)
        ExerciseModel("65", "Esteira Caminhada", "9", MeasurementUnit.DISTANCE, 1),
        ExerciseModel("66", "Esteira Corrida", "9", MeasurementUnit.DISTANCE, 2),
        ExerciseModel("67", "Bicicleta Ergométrica", "9", MeasurementUnit.DISTANCE, 3),
        ExerciseModel("68", "Elíptico", "9", MeasurementUnit.DISTANCE, 4),
        ExerciseModel("69", "Escada", "9", MeasurementUnit.TIME, 5),
        ExerciseModel("70", "Remo", "9", MeasurementUnit.DISTANCE, 6),
        ExerciseModel("71", "Pular Corda", "9", MeasurementUnit.TIME, 7),
        ExerciseModel("72", "Corrida Externa", "9", MeasurementUnit.DISTANCE, 8),
        ExerciseModel("73", "Caminhada", "9", MeasurementUnit.DISTANCE, 9),
        ExerciseModel("74", "HIIT", "9", MeasurementUnit.TIME, 10),

        // Glutes (10)
        ExerciseModel("75", "Elevação Pélvica", "10", MeasurementUnit.WEIGHT, 1),
        ExerciseModel("76", "Hip Thrust", "10", MeasurementUnit.WEIGHT, 2, false),
        ExerciseModel("77", "Coice na Polia", "10", MeasurementUnit.WEIGHT, 3),
        ExerciseModel("78", "Abdução de Quadril", "10", MeasurementUnit.WEIGHT, 4),
        ExerciseModel("79", "Agachamento Sumô", "10", MeasurementUnit.WEIGHT, 5),
        ExerciseModel("80", "Stiff", "10", MeasurementUnit.WEIGHT, 6),
        ExerciseModel("81", "Afundo", "10", MeasurementUnit.WEIGHT, 7),
        ExerciseModel("82", "Passada", "10", MeasurementUnit.WEIGHT, 8),
        ExerciseModel("83", "Glúteo Máquina", "10", MeasurementUnit.WEIGHT, 9),
        ExerciseModel("84", "Ponte de Glúteo", "10", MeasurementUnit.REPS, 10),

        // Calves (11)
        ExerciseModel("85", "Panturrilha em Pé", "11", MeasurementUnit.WEIGHT, 1),
        ExerciseModel("86", "Panturrilha Sentado", "11", MeasurementUnit.WEIGHT, 2),
        ExerciseModel("87", "Panturrilha no Leg Press", "11", MeasurementUnit.WEIGHT, 3),
        ExerciseModel("88", "Panturrilha Smith", "11", MeasurementUnit.WEIGHT, 4),
        ExerciseModel("89", "Panturrilha Unilateral", "11", MeasurementUnit.WEIGHT, 5),
        ExerciseModel("90", "Panturrilha no Hack", "11", MeasurementUnit.WEIGHT, 6),
        ExerciseModel("91", "Elevação de Panturrilha Degrau", "11", MeasurementUnit.REPS, 7),
    )

    private val suggestions = listOf(
        GoalSuggestion("1", "Lose weight", isWeightGoal = true),
        GoalSuggestion("2", "Gain muscle", isWeightGoal = true),
        GoalSuggestion("3", "Increase strength", ExerciseCategory.STRENGTH, muscleGroupId = "3"),
        GoalSuggestion("4", "Improve pace", ExerciseCategory.CARDIO, muscleGroupId = "9"),
        GoalSuggestion("5", "Strong core", ExerciseCategory.STRENGTH, muscleGroupId = "8"),
        GoalSuggestion("6", "Steel chest", ExerciseCategory.STRENGTH, muscleGroupId = "2")
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
