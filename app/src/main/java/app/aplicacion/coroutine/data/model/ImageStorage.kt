package app.aplicacion.coroutine.data.model

data class ImageStorage (
    val uid:List<String>,
    val donwload:List<String>
        ) {
    constructor() :this(emptyList(), emptyList())
}