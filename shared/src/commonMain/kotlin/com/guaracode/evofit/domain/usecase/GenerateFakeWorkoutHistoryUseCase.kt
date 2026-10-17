package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.core.common.DateMapper
import com.guaracode.evofit.domain.model.*
import com.guaracode.evofit.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.*
import kotlin.math.pow
import kotlin.random.Random

interface GenerateFakeWorkoutHistoryUseCase {
    suspend operator fun invoke()
}

class GenerateFakeWorkoutHistoryUseCaseImpl(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getMuscleGroupsUseCase: GetMuscleGroupsUseCase,
    private val getExercisesByGroupUseCase: GetExercisesByGroupUseCase,
    private val saveWorkoutDoneUseCase: SaveWorkoutDoneUseCase,
    private val onboardingRepository: OnboardingRepository
) : GenerateFakeWorkoutHistoryUseCase {

    override suspend fun invoke() {
        val userId = getUserIdUseCase().firstOrNull() ?: return
        
        generateFakeWeightHistory(userId)
        
        val muscleGroups = getMuscleGroupsUseCase()
        if (muscleGroups.isEmpty()) return

        val muscleGroupsWithExercises = muscleGroups.map { group ->
            group to getExercisesByGroupUseCase(group.id)
        }.filter { it.second.isNotEmpty() }

        if (muscleGroupsWithExercises.size < 3) return

        val templates = List(7) { _ ->
            val selectedGroups = muscleGroupsWithExercises.shuffled().take(3)
            selectedGroups.mapIndexed { groupIndex, (group, exercises) ->
                val selectedExercises = exercises.shuffled().take(4)
                group to selectedExercises
            }
        }

        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val startDate = today.minus(6, DateTimeUnit.MONTH)

        var currentLocalDate = startDate
        val random = Random.Default

        while (currentLocalDate <= today) {
            val workoutDays = mutableSetOf<Int>()
            while (workoutDays.size < 4) {
                workoutDays.add(random.nextInt(7))
            }

            val weekStartDate = currentLocalDate
            for (dayOffset in 0..6) {
                val workoutDate = weekStartDate.plus(dayOffset, DateTimeUnit.DAY)
                if (workoutDate > today) break

                if (workoutDays.contains(dayOffset)) {
                    val template = templates[random.nextInt(templates.size)]
                    val monthsPassed = ((workoutDate.year - startDate.year) * 12 + (workoutDate.monthNumber - startDate.monthNumber)).coerceAtLeast(0)
                    val evolutionFactor = 1.05.pow(monthsPassed.toDouble())

                    saveFakeWorkout(userId, workoutDate, template, evolutionFactor)
                }
            }
            currentLocalDate = currentLocalDate.plus(7, DateTimeUnit.DAY)
        }
    }

    private suspend fun saveFakeWorkout(
        userId: String,
        date: LocalDate,
        template: List<Pair<MuscleGroup, List<Exercise>>>,
        evolutionFactor: Double
    ) {
        val workoutId = randomUUID()
        val dateStr = "${date.dayOfMonth.toString().padStart(2, '0')}/${date.monthNumber.toString().padStart(2, '0')}/${date.year}"
        
        val exercisesByGroup = template.mapIndexed { groupIndex, (group, exercises) ->
            val workoutExerciseUuid = randomUUID()
            
            val workoutExercises = exercises.mapIndexed { exIndex, exercise ->
                val exerciseUuid = randomUUID()
                
                val baseValue = when (exercise.unit) {
                    MeasurementUnit.DISTANCE -> 2.0 + Random.Default.nextInt(3)
                    MeasurementUnit.TIME -> 10.0 + Random.Default.nextInt(20)
                    else -> 20.0 + Random.Default.nextInt(20)
                }
                
                val evolvedValue = baseValue * evolutionFactor

                val sets = List(3) { setIndex ->
                    val seriesFactor = 1.0 - (setIndex * 0.1)
                    val valueForSeries = (evolvedValue * seriesFactor).coerceAtLeast(0.1)
                    
                    when (exercise.unit) {
                        MeasurementUnit.DISTANCE -> {
                            val distanceValue = valueForSeries.toInt().toDouble().coerceAtLeast(1.0)
                            ExerciseSet(
                                id = exercise.id,
                                exerciseName = exercise.name,
                                workoutExerciseId = exerciseUuid,
                                setNumber = setIndex + 1,
                                reps = 0,
                                load = 0.0,
                                unit = exercise.unit,
                                distance = distanceValue,
                                time = (distanceValue * (8 + Random.Default.nextInt(4))).toInt()
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
                                reps = 10 + Random.Default.nextInt(5),
                                load = valueForSeries.toInt().toDouble().coerceAtLeast(1.0),
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
        val epochMillis = date.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()

        val workoutDone = WorkoutDone(
            id = workoutId,
            userId = userId,
            name = workoutName,
            date = dateStr,
            exercisesByGroup = exercisesByGroup,
            time = "00:${45 + Random.Default.nextInt(30)}:00",
            createdAt = epochMillis
        )

        saveWorkoutDoneUseCase(userId, workoutDone)
    }

    private suspend fun generateFakeWeightHistory(userId: String) {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val startDate = today.minus(6, DateTimeUnit.MONTH)
        
        var currentLocalDate = startDate
        var currentWeight = 85.0 + Random.Default.nextInt(10)
        
        while (currentLocalDate <= today) {
            val recordsInMonth = 1 + Random.Default.nextInt(3)
            
            for (i in 0 until recordsInMonth) {
                val dayOffset = (i * 10) + Random.Default.nextInt(5)
                val weightDate = try {
                    currentLocalDate.plus(dayOffset, DateTimeUnit.DAY)
                } catch (e: Exception) {
                    currentLocalDate
                }
                
                if (weightDate > today) break
                
                currentWeight -= (0.2 + Random.Default.nextDouble() * 0.6)
                val dateStr = "${weightDate.dayOfMonth.toString().padStart(2, '0')}/${weightDate.monthNumber.toString().padStart(2, '0')}/${weightDate.year}"
                
                val weightUpdate = WeightUpdate(
                    id = randomUUID(),
                    weight = "${(currentWeight * 10).toInt() / 10.0}".replace(".", ","),
                    date = dateStr
                )
                
                onboardingRepository.saveWeightUpdate(weightUpdate, userId)
            }
            
            currentLocalDate = currentLocalDate.plus(1, DateTimeUnit.MONTH)
        }
    }

    private fun randomUUID(): String {
        return "fake-${Clock.System.now().toEpochMilliseconds()}-${Random.Default.nextInt(100000)}"
    }
}
