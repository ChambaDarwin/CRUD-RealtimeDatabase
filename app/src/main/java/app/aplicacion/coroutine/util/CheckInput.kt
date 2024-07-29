package app.aplicacion.coroutine.util

import android.text.TextUtils
import android.util.Patterns

fun validateName(nombre:String):ValidateField{
    if(TextUtils.isEmpty(nombre)){
        return ValidateField.Error("campo nombre requerido")
    }
    return ValidateField.Succes

}
 fun validateEmail(email:String):ValidateField{
    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        return ValidateField.Error("formato de email no valido")

    }
    return ValidateField.Succes
}
fun validateApellido(apellido:String):ValidateField{
    if(TextUtils.isEmpty(apellido)){
        return ValidateField.Error("campo apellido requerido")
    }
    return ValidateField.Succes

}
fun validateMateria(materia:String):ValidateField{
    if(TextUtils.isEmpty(materia)){
        return ValidateField.Error("campo materia requerido")

    }
    return ValidateField.Succes
}