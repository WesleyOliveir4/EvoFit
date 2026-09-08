package com.example.evofit.domain.usecase

import com.example.evofit.domain.repository.AuthRepository
import com.example.evofit.domain.repository.OnboardingRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class AddWeightUpdateUseCaseTest {

    private val repository: OnboardingRepository = mockk()
    private val authRepository: AuthRepository = mockk()
    private val useCase = AddWeightUpdateUseCaseImpl(repository, authRepository)

    @Test
    fun `when weight is added, it should call repository with current date in PT-BR`() = runBlocking {
        // Arrange
        val userId = "user123"
        val weight = "80.5"
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
        val expectedDate = sdf.format(Date())

        coEvery { authRepository.getCurrentUserId() } returns userId
        coEvery { repository.saveWeightUpdate(any(), any()) } returns Unit

        // Act
        useCase(weight)

        // Assert
        coVerify {
            repository.saveWeightUpdate(
                match { it.weight == weight && it.date == expectedDate },
                userId
            )
        }
    }
    
    @Test
    fun `when userId is null, it should not call repository`() = runBlocking {
        // Arrange
        val weight = "80.5"
        coEvery { authRepository.getCurrentUserId() } returns null

        // Act
        useCase(weight)

        // Assert
        coVerify(exactly = 0) {
            repository.saveWeightUpdate(any(), any())
        }
    }
}
