package app.aplicacion.coroutine.util

sealed class DataState<T>(
    val data:T? = null,
    val message:String ?=null
){
    class Loading<T>():DataState<T>()
    class Error<T>(message: String?):DataState<T>(null,message)
    class Sucess<T>(data: T?):DataState<T>(data ,null)

}
