package iberoplast.pe.gespro.model.ubigeo_peru

data class Department(
    val id:String,
    val name:String
) {
    override fun toString(): String {
        return name
    }

//    override fun toString(): String {
//        return "ID: $id, Name: $name"
//    }
}
