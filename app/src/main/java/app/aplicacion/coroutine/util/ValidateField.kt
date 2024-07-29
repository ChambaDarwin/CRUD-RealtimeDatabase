package app.aplicacion.coroutine.util

sealed class ValidateField{
    data class Error (val message:String):ValidateField()
    object Succes:ValidateField()
}
data class ValidateUser(
    val nombre:ValidateField,
    val apellido:ValidateField,
    val materia:ValidateField,
    val email:ValidateField
)