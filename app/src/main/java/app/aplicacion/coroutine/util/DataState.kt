package app.aplicacion.coroutine.util

sealed class DataState<T>(
    val data:T?=null,
    val message:String?=null
) {
    class Error<T>(message: String?,data: T?=null): DataState<T>(data, message)
    class Sucess<T>(data: T?=null): DataState<T>(data)
    class Loading<T>():DataState<T>()
}

