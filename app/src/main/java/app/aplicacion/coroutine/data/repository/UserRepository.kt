package app.aplicacion.coroutine.data.repository

import androidx.lifecycle.LiveData
import app.aplicacion.coroutine.data.model.ImageStorage
import app.aplicacion.coroutine.data.model.UserData
import app.aplicacion.coroutine.util.DataState


interface UserRepository {
    suspend fun insertUser(user: UserData, state: (DataState<String>) -> Unit)
    suspend fun insertCloudStorage(
        byteArray: List<ByteArray>,
        state: (DataState<ImageStorage>) -> Unit
    )

    suspend fun deleteUser(user: UserData, state: (DataState<String>) -> Unit)
    suspend fun updateUser(user: UserData, state: (DataState<String>) -> Unit)
    fun getAllUser(lista: (MutableList<UserData>) -> Unit)
}
