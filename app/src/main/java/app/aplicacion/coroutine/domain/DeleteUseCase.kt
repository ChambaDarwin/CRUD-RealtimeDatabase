package app.aplicacion.coroutine.domain

import app.aplicacion.coroutine.data.model.UserData
import app.aplicacion.coroutine.data.repository.UserImplementation
import app.aplicacion.coroutine.util.DataState
import java.lang.Exception
import javax.inject.Inject

class DeleteUseCase @Inject constructor(
    private val repository: UserImplementation
) {
  suspend fun deleteUser(user: UserData): DataState<String> {
        return try {
           val result= repository.deleteUser(user)

            when (result) {
                is DataState.Error -> DataState.Error(result.message)
                is DataState.Loading -> DataState.Loading()
                is DataState.Sucess -> DataState.Sucess(result.data)
            }


        } catch (e: Exception) {
            e.printStackTrace()
            DataState.Error(e.message)
        }

    }

}
