package app.aplicacion.coroutine.data.repository

import androidx.lifecycle.LiveData
import app.aplicacion.coroutine.data.model.UserData
import app.aplicacion.coroutine.util.DataState


interface UserRepository {
    fun insertUser(user: UserData,state:(DataState<Boolean>)->Unit)
    fun deleteUser(user:UserData)
    fun updateUser(user: UserData,state:(DataState<Boolean>)->Unit)
    fun getAllUser(lista:(MutableList<UserData>)->Unit)
}
