package app.aplicacion.coroutine.domain

import android.service.autofill.UserData
import app.aplicacion.coroutine.data.model.ImageStorage
import app.aplicacion.coroutine.data.repository.UserImplementation
import app.aplicacion.coroutine.util.DataState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*


class UpdateUseCaseTest {
    @RelaxedMockK
    private lateinit var repository: UserImplementation
    private lateinit var updateCase: UpdateUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        updateCase = UpdateUseCase(repository)
    }

    @Test
    fun verificarUpdateUser() = runBlocking {
        // Given
        val userData = app.aplicacion.coroutine.data.model.UserData(
            "1234",
            "Darwin","chamba","darwin@gmailcom","fisica", ImageStorage(listOf("sdds","sfds"),
                listOf("sf","ds")
            )
        ) // Crear un objeto UserData de prueba
        val expectedResult = DataState.Sucess("User updated successfully") // Resultado esperado
        coEvery { repository.updateUser(userData) } returns expectedResult

        // When
        val result = updateCase.updateUser(userData)

        // Then
        coVerify (exactly = 1) { repository.updateUser(userData) }
        assertEquals(expectedResult, result)

    }



}