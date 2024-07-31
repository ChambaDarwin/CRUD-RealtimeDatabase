package app.aplicacion.coroutine.data.repository

import androidx.lifecycle.LiveData
import app.aplicacion.coroutine.data.model.ImageStorage
import app.aplicacion.coroutine.data.model.UserData
import app.aplicacion.coroutine.util.DataState
import kotlinx.coroutines.flow.Flow


interface UserRepository {
    suspend fun insertUser(user: UserData, state: (DataState<String>) -> Unit)
    suspend fun insertCloudStorage(
        byteArray: List<ByteArray>,
        state: (DataState<ImageStorage>) -> Unit
    )

    suspend fun deleteUser(user: UserData): DataState<String>
    suspend fun updateUser(user: UserData):DataState<String>
    fun getAllUser(lista: (DataState<MutableList<UserData>>) -> Unit)
}
