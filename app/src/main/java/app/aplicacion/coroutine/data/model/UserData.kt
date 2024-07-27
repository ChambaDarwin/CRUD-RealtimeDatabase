package app.aplicacion.coroutine.data.model

import java.io.Serializable

data class UserData (
    var id:String,
    val nombre:String,
    val apellido:String,
    val email:String,
    val materia:String,
   var image:ImageStorage?
        ):Serializable{
    constructor() :this("","","","","", null)
    fun toMap():Map<String,Any>{
        return mapOf(
            "id" to id,
            "nombre" to nombre,
            "apellido" to apellido,
            "email" to email,
            "materia" to materia,
            "image" to image!!.convertToMap()



        )
    }

}