package com.guaracode.evofit.domain.usecase

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

        val now = Clock.System.now()
        val timeZone = TimeZone.currentSystemDefault()
        val endDate = now.toLocalDateTime(timeZone).date
        val startDate = endDate.minus(6, DateTimeUnit.MONTH)

        var currentLocalDate = startDate
        val random = Random(now.toEpochMilliseconds())

        while (currentLocalDate <= endDate) {
            val workoutDays = mutableSetOf<Int>()
            while (workoutDays.size < 4) {
                workoutDays.add(random.nextInt(7))
            }

            for (dayOffset in 0..6) {
                val workoutDate = currentLocalDate.plus(dayOffset, DateTimeUnit.DAY)
                if (workoutDate > endDate) break

                if (workoutDays.contains(dayOffset)) {
                    val template = templates[random.nextInt(templates.size)]
                    val monthsPassed = getMonthsBetween(startDate, workoutDate)
                    val evolutionFactor = 1.05.pow(monthsPassed.toDouble())

                    val millis = workoutDate.atStartOfDayIn(timeZone).toEpochMilliseconds()
                    saveFakeWorkout(userId, millis, template, evolutionFactor)
                }
            }
            
            currentLocalDate = currentLocalDate.plus(7, DateTimeUnit.DAY)
        }
    }

    private suspend fun saveFakeWorkout(
        userId: String,
        timestamp: Long,
        template: List<Pair<MuscleGroup, List<Exercise>>>,
        evolutionFactor: Double
    ) {
        val workoutId = randomId()
        
        val exercisesByGroup = template.mapIndexed { groupIndex, (group, exercises) ->
            val workoutExerciseUuid = randomId()
            
            val workoutExercises = exercises.mapIndexed { exIndex, exercise ->
                val exerciseUuid = randomId()
                
                val baseValue = when (exercise.unit) {
                    MeasurementUnit.DISTANCE -> 2.0 + Random.nextInt(3)
                    MeasurementUnit.TIME -> 10.0 + Random.nextInt(20)
                    else -> 20.0 + Random.nextInt(20)
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
                                time = (distanceValue * (8 + Random.nextInt(4))).toInt()
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
                                reps = 10 + Random.nextInt(5),
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
        val dateStr = formatDate(timestamp)

        val workoutDone = WorkoutDone(
            id = randomId(),
            userId = userId,
            name = workoutName,
            date = dateStr,
            exercisesByGroup = exercisesByGroup,
            time = "00:${45 + Random.nextInt(30)}:00",
            createdAt = timestamp
        )

        saveWorkoutDoneUseCase(userId, workoutDone)
    }

    private fun getMonthsBetween(start: LocalDate, end: LocalDate): Int {
        val years = end.year - start.year
        val months = end.monthNumber - start.monthNumber
        return years * 12 + months
    }

    private suspend fun generateFakeWeightHistory(userId: String) {
        val timeZone = TimeZone.currentSystemDefault()
        val endDate = Clock.System.now().toLocalDateTime(timeZone).date
        val startDate = endDate.minus(6, DateTimeUnit.MONTH)
        
        var currentLocalDate = startDate
        var currentWeight = 85.0 + Random.nextInt(10)
        
        while (currentLocalDate <= endDate) {
            val recordsInMonth = 1 + Random.nextInt(3)
            
            for (i in 0 until recordsInMonth) {
                val dayOffset = (i * 10) + Random.nextInt(5)
                val weightDate = currentLocalDate.plus(dayOffset, DateTimeUnit.DAY)
                if (weightDate > endDate) break
                
                currentWeight -= (0.2 + Random.nextDouble() * 0.6)
                val millis = weightDate.atStartOfDayIn(timeZone).toEpochMilliseconds()
                
                val weightUpdate = WeightUpdate(
                    id = randomId(),
                    weight = ((currentWeight * 10).toInt() / 10.0).toString().replace(".", ","),
                    date = formatDate(millis)
                )
                
                onboardingRepository.saveWeightUpdate(weightUpdate, userId)
            }
            
            currentLocalDate = currentLocalDate.plus(1, DateTimeUnit.MONTH)
        }
    }

    private fun formatDate(epochMillis: Long): String {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
        val month = localDateTime.monthNumber.toString().padStart(2, '0')
        val year = localDateTime.year
        return "$day/$month/$year"
    }

    private fun randomId(): String {
        return Random.nextBits(32).toString()
    }
}
