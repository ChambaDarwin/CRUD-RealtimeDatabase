package app.aplicacion.coroutine.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.aplicacion.coroutine.data.model.UserData
import app.aplicacion.coroutine.data.repository.UserImplementation
import app.aplicacion.coroutine.data.repository.UserRepository
import app.aplicacion.coroutine.util.DataState
import app.aplicacion.coroutine.util.ValidateField
import app.aplicacion.coroutine.util.ValidateUser
import app.aplicacion.coroutine.util.validateEmail
import app.aplicacion.coroutine.util.validateName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository:UserImplementation
):ViewModel() {

    private val _lista= MutableLiveData<List<UserData>>()
    val lista:LiveData<List<UserData>> = _lista
    private val _insertData=MutableLiveData<DataState<Boolean>>()
    val insertData:LiveData<DataState<Boolean>> = _insertData

    private val _updateData=MutableLiveData<DataState<Boolean>>()
    val updateData:LiveData<DataState<Boolean>> = _updateData

    private val _validatUser= Channel<ValidateUser> ()
    val validateUser = _validatUser.receiveAsFlow()



    init {
        getAllUser()
    }

    private fun validateUserInput(user:UserData):Boolean {
        val nameValidate= validateName(user.nombre)
        val emailValidate= validateEmail(user.email)
        return nameValidate is ValidateField.Succes && emailValidate is ValidateField.Succes
    }
    fun inserUser(user:UserData){
        viewModelScope.launch {
            if(validateUserInput(user)){
                _insertData.value=DataState.Loading()
                repository.insertUser(user){
                    _insertData.value=it
                }
            }else{
                val validateUserField=ValidateUser(
                    validateName(user.nombre),
                    validateEmail(user.email)
                )
                runBlocking {
                    _validatUser.send(validateUserField)
                }


            }
        }


    }

    fun updateUser(user:UserData){

        if(validateUserInput(user)){
            _updateData.value=DataState.Loading()
            repository.updateUser(user){
                _updateData.value=it
            }
        }else{
            val validateUserField=ValidateUser(
                validateEmail(user.email),
                validateName(user.nombre)

            )
            runBlocking {
                _validatUser.send(validateUserField)
            }


        }
    }


    fun deleteUser(user:UserData){
        repository.deleteUser(user)
    }
    fun getAllUser(){
        repository.getAllUser {
            _lista.value=it
        }
    }
}