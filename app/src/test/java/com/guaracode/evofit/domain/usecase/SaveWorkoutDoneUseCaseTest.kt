package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.domain.model.WorkoutDone
import com.guaracode.evofit.domain.repository.WorkoutRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class SaveWorkoutDoneUseCaseTest {

    private val repository: WorkoutRepository = mockk(relaxed = true)
    private val useCase = SaveWorkoutDoneUseCaseImpl(repository)

    @Test
    fun `should delegate saveWorkoutDone to repository`() = runBlocking {
        val workoutDone = WorkoutDone(id = "wd1", userId = "u1", name = "Treino Concluído")

        useCase("u1", workoutDone)

        coVerify { repository.saveWorkoutDone("u1", workoutDone) }
    }
}
