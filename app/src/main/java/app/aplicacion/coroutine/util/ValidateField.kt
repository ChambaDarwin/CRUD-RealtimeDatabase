package app.aplicacion.coroutine.util

sealed class ValidateField{
    data class Error (val message:String):ValidateField()
    object Succes:ValidateField()
}
data class ValidateUser(
    val email:ValidateField,
    val nombre:ValidateField
)