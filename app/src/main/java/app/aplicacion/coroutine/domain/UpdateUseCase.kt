package app.aplicacion.coroutine.domain

import androidx.lifecycle.viewModelScope
import app.aplicacion.coroutine.data.model.UserData
import app.aplicacion.coroutine.data.repository.UserImplementation
import app.aplicacion.coroutine.util.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpdateUseCase @Inject constructor(
    private val repository:UserImplementation
) {


   suspend fun updateUser(userData: UserData):DataState<String> {

           return repository.updateUser( userData)


    }
}