package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.repository.WorkoutRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class UpdateWorkoutsOrderUseCaseTest {

    private val repository: WorkoutRepository = mockk(relaxed = true)
    private val useCase = UpdateWorkoutsOrderUseCaseImpl(repository)

    @Test
    fun `should update orderIndex based on list index and delegate to repository`() = runBlocking {
        val w1 = Workout("1", "u1", "T1", "date", emptyList(), 10)
        val w2 = Workout("2", "u1", "T2", "date", emptyList(), 20)

        useCase(listOf(w1, w2))

        coVerify {
            repository.updateWorkoutsOrder(
                match { list ->
                    list.size == 2 &&
                            list[0].id == "1" && list[0].orderIndex == 0 &&
                            list[1].id == "2" && list[1].orderIndex == 1
                }
            )
        }
    }
}
