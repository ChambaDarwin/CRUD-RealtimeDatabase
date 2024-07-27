package app.aplicacion.coroutine.data.model

data class ImageStorage (
    val uid:List<String>,
    val donwload:List<String>
        ) {
    constructor() :this(emptyList(), emptyList())

    fun convertToMap():Map<String,Any>{
        return mapOf(
            "uid" to uid,
        "donwload" to donwload)
    }
}