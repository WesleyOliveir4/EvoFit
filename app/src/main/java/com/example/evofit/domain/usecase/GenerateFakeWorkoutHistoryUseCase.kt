package com.example.evofit.domain.usecase

import com.example.evofit.core.common.DateMapper
import com.example.evofit.domain.model.*
import kotlinx.coroutines.flow.firstOrNull
import java.util.*
import kotlin.math.pow

interface GenerateFakeWorkoutHistoryUseCase {
    suspend operator fun invoke()
}

class GenerateFakeWorkoutHistoryUseCaseImpl(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getMuscleGroupsUseCase: GetMuscleGroupsUseCase,
    private val getExercisesByGroupUseCase: GetExercisesByGroupUseCase,
    private val saveWorkoutDoneUseCase: SaveWorkoutDoneUseCase
) : GenerateFakeWorkoutHistoryUseCase {

    override suspend fun invoke() {
        val userId = getUserIdUseCase().firstOrNull() ?: return
        val muscleGroups = getMuscleGroupsUseCase()
        if (muscleGroups.isEmpty()) return

        val muscleGroupsWithExercises = muscleGroups.map { group ->
            group to getExercisesByGroupUseCase(group.id)
        }.filter { it.second.isNotEmpty() }

        if (muscleGroupsWithExercises.size < 3) return

        // 1. Criar 7 Modelos de Treino (Templates)
        // Cada modelo tem 3 grupos e 4 exercícios por grupo
        val templates = List(7) { templateIndex ->
            // Seleciona 3 grupos aleatórios (ou rotacionados para garantir variedade)
            val selectedGroups = muscleGroupsWithExercises.shuffled().take(3)
            
            selectedGroups.mapIndexed { groupIndex, (group, exercises) ->
                val selectedExercises = exercises.shuffled().take(4)
                group to selectedExercises
            }
        }

        // 2. Definir período: 6 meses atrás até hoje
        val calendar = Calendar.getInstance()
        val endDate = calendar.time
        calendar.add(Calendar.MONTH, -6)
        val startDate = calendar.time

        // 3. Gerar treinos (4 por semana)
        val currentCalendar = Calendar.getInstance()
        currentCalendar.time = startDate

        val random = Random()

        while (!currentCalendar.time.after(endDate)) {
            // Para cada semana, escolhe 4 dias aleatórios (0 a 6)
            val workoutDays = mutableSetOf<Int>()
            while (workoutDays.size < 4) {
                workoutDays.add(random.nextInt(7))
            }

            val weekStartDate = currentCalendar.time
            
            for (dayOffset in 0..6) {
                val workoutCalendar = Calendar.getInstance()
                workoutCalendar.time = weekStartDate
                workoutCalendar.add(Calendar.DAY_OF_YEAR, dayOffset)
                
                if (workoutCalendar.time.after(endDate)) break

                if (workoutDays.contains(dayOffset)) {
                    // Escolhe um template aleatório
                    val template = templates[random.nextInt(templates.size)]
                    
                    // Calcula evolução de carga: +5% ao mês
                    val monthsPassed = getMonthsBetween(startDate, workoutCalendar.time)
                    val evolutionFactor = 1.05.pow(monthsPassed.toDouble())

                    saveFakeWorkout(userId, workoutCalendar.time, template, evolutionFactor)
                }
            }
            
            currentCalendar.add(Calendar.WEEK_OF_YEAR, 1)
        }
    }

    private suspend fun saveFakeWorkout(
        userId: String,
        date: Date,
        template: List<Pair<MuscleGroup, List<Exercise>>>,
        evolutionFactor: Double
    ) {
        val workoutId = UUID.randomUUID().toString()
        
        val exercisesByGroup = template.mapIndexed { groupIndex, (group, exercises) ->
            val workoutExerciseUuid = UUID.randomUUID().toString()
            
            val workoutExercises = exercises.mapIndexed { exIndex, exercise ->
                val exerciseUuid = UUID.randomUUID().toString()
                
                // Carga base dependendo da unidade
                val baseValue = when (exercise.unit) {
                    MeasurementUnit.DISTANCE -> 2.0 + Random().nextInt(3) // 2-5 km base
                    MeasurementUnit.TIME -> 10.0 + Random().nextInt(20)   // 10-30 min base
                    else -> 20.0 + Random().nextInt(20)                  // 20-40 kg base
                }
                
                val evolvedValue = baseValue * evolutionFactor

                val sets = List(3) { setIndex ->
                    val seriesFactor = 1.0 - (setIndex * 0.1)
                    val valueForSeries = (evolvedValue * seriesFactor).coerceAtLeast(0.1)
                    
                    when (exercise.unit) {
                        MeasurementUnit.DISTANCE -> {
                            ExerciseSet(
                                id = exercise.id,
                                exerciseName = exercise.name,
                                workoutExerciseId = exerciseUuid,
                                setNumber = setIndex + 1,
                                reps = 0,
                                load = 0.0,
                                unit = exercise.unit,
                                distance = valueForSeries,
                                time = (valueForSeries * (8 + Random().nextInt(4))).toInt() // 8-12 min por km
                            )
                        }
                        MeasurementUnit.TIME -> {
                            ExerciseSet(
                                id = exercise.id,
                                exerciseName = exercise.name,
                                workoutExerciseId = exerciseUuid,
                                setNumber = setIndex + 1,
                                reps = 0,
                                load = 0.0,
                                unit = exercise.unit,
                                time = valueForSeries.toInt().coerceAtLeast(1),
                                distance = null
                            )
                        }
                        else -> {
                            ExerciseSet(
                                id = exercise.id,
                                exerciseName = exercise.name,
                                workoutExerciseId = exerciseUuid,
                                setNumber = setIndex + 1,
                                reps = 10 + Random().nextInt(5),
                                load = valueForSeries,
                                unit = exercise.unit,
                                time = null,
                                distance = null
                            )
                        }
                    }
                }

                WorkoutExercise(
                    id = exerciseUuid,
                    exerciseId = exercise.id,
                    sets = sets,
                    totalSets = sets.size,
                    orderIndex = exIndex
                )
            }

            WorkoutGroup(
                muscleGroupId = group.id,
                muscleGroup = group,
                orderIndex = groupIndex,
                exercises = workoutExercises
            )
        }

        val workoutName = template.joinToString(" + ") { it.first.name }

        val workoutDone = WorkoutDone(
            id = UUID.randomUUID().toString(),
            userId = userId,
            name = workoutName,
            date = DateMapper.formatDate(date),
            exercisesByGroup = exercisesByGroup,
            time = "00:${45 + Random().nextInt(30)}:00",
            createdAt = date.time
        )

        saveWorkoutDoneUseCase(userId, workoutDone)
    }

    private fun getMonthsBetween(start: Date, end: Date): Int {
        val startCal = Calendar.getInstance().apply { time = start }
        val endCal = Calendar.getInstance().apply { time = end }
        
        val years = endCal.get(Calendar.YEAR) - startCal.get(Calendar.YEAR)
        val months = endCal.get(Calendar.MONTH) - startCal.get(Calendar.MONTH)
        
        return years * 12 + months
    }
}
