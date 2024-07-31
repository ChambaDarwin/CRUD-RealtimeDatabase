package app.aplicacion.coroutine.util

sealed class DataState<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Sucess<T>(data: T?) : DataState<T>( data)
    class Loading<T> : DataState<T>()
    class Error<T>(message: String?) : DataState<T>(message = message)
}


