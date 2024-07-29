package app.aplicacion.coroutine.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import app.aplicacion.coroutine.core.hideProgressBar
import app.aplicacion.coroutine.core.showProgressBar
import app.aplicacion.coroutine.data.model.ImageStorage
import app.aplicacion.coroutine.data.model.UserData
import app.aplicacion.coroutine.data.repository.UserImplementation
import app.aplicacion.coroutine.data.repository.UserRepository
import app.aplicacion.coroutine.databinding.FragmentAddBinding
import app.aplicacion.coroutine.util.DataState
import app.aplicacion.coroutine.util.ValidateField
import app.aplicacion.coroutine.util.ValidateUser
import app.aplicacion.coroutine.util.validateApellido
import app.aplicacion.coroutine.util.validateEmail
import app.aplicacion.coroutine.util.validateMateria
import app.aplicacion.coroutine.util.validateName
import com.bumptech.glide.Glide.init
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserImplementation
) : ViewModel() {

    private val _lista = MutableLiveData<List<UserData>>()
    val lista: LiveData<List<UserData>> = _lista

    private val _insertData = MutableLiveData<DataState<String>>()
    val insertData: LiveData<DataState<String>> get() = _insertData

    private val _storeCloud = MutableLiveData<DataState<ImageStorage>>()
    val storeCloud: LiveData<DataState<ImageStorage>> = _storeCloud

    private val _updateData = MutableLiveData<DataState<String>>()
    val updateData: LiveData<DataState<String>> get() = _updateData

    private val _obserDelete = MutableLiveData<DataState<String>>()
    val observeDelete: LiveData<DataState<String>> get() = _obserDelete

    private val _validatUser = Channel<ValidateUser>()
    val validateUser = _validatUser.receiveAsFlow()


    init {
        getAllUser()
    }

    fun getAllUser() {
        repository.getAllUser {
            _lista.value = it
        }
    }

    fun insertUser(user: UserData) {

            viewModelScope.launch {
                _insertData.value = DataState.Loading()
                repository.insertUser(user) { _insertData.value = it }
            }


    }

    fun insertImage(byteArray: List<ByteArray>) {

        viewModelScope.launch {
            _storeCloud.value = DataState.Loading()
            repository.insertCloudStorage(byteArray) { _storeCloud.value = it }
        }
    }

    fun updateUser(user: UserData) {
        if (validateUserInput(user)) {

            viewModelScope.launch {
                _updateData.value = DataState.Loading()
                repository.updateUser(user) {
                    _updateData.value = it
                }
            }
        } else {
            val validateUserField = ValidateUser(
                validateName(user.email),
                validateApellido(user.nombre),
                validateMateria(user.email),
                validateEmail(user.nombre)

            )
            runBlocking {
                _validatUser.send(validateUserField)
            }
        }


    }
    fun deleteUser(user: UserData) {
        _obserDelete.value = DataState.Loading()
        viewModelScope.launch {
            repository.deleteUser(user) { _obserDelete.value = it }
        }

    }


    private fun validateUserInput(user: UserData): Boolean {
        val nameValidate = validateName(user.nombre)
        val emailValidate = validateEmail(user.email)
        val apellidoValidate = validateApellido(user.apellido)
        val materiaValidate = validateMateria(user.materia)
        return nameValidate is ValidateField.Succes && emailValidate is ValidateField.Succes
                && apellidoValidate is ValidateField.Succes && materiaValidate is ValidateField.Succes
    }

}













