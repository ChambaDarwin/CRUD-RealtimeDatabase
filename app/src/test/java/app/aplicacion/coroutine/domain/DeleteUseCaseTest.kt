package app.aplicacion.coroutine.domain

import android.service.autofill.UserData
import app.aplicacion.coroutine.data.model.ImageStorage
import app.aplicacion.coroutine.data.repository.UserImplementation
import app.aplicacion.coroutine.data.repository.UserRepository
import app.aplicacion.coroutine.util.DataState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class DeleteUseCaseTest {
    @RelaxedMockK
    private lateinit var repository: UserImplementation
    private lateinit var deleteUseCase: DeleteUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        deleteUseCase = DeleteUseCase(repository)
    }

    @Test
    fun verifivarDeleteUser() = runBlocking {


// Given
        val user = getUserData()
        val expectedResult = DataState.Sucess("registro realizado con exito")
        coEvery { repository.deleteUser(user) } returns expectedResult

        // When
        val result =
            deleteUseCase.deleteUser(user) // Llamamos al método y capturamos el valor de retorno

        // Then
        coVerify { repository.deleteUser(user) } // Verificamos que se llamó al método con los argumentos correctos
        assert(result is DataState.Sucess)
        assertEquals(expectedResult.data, (result as DataState.Sucess).data)


    }



    fun getUserData() = app.aplicacion.coroutine.data.model.UserData(
        "1234",
        "Darwin", "chamba", "darwin@gmailcom", "fisica", ImageStorage(
            listOf("sdds", "sfds"),
            listOf("sf", "ds")
        )

    )

}