package app.aplicacion.coroutine.util

import android.util.Patterns

fun validateName(nombre:String):ValidateField{
    if(nombre.isNullOrEmpty()){
        return ValidateField.Error("No puede ser vacio")
    }
    return ValidateField.Succes

}
 fun validateEmail(email:String):ValidateField{
    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        return ValidateField.Error("formato de email no valido")

    }
    return ValidateField.Succes
}