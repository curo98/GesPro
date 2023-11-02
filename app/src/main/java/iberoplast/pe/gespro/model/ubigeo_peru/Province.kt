package iberoplast.pe.gespro.model.ubigeo_peru

data class Province(
    val id:String,
    val name:String,
    val department_id: Int,

    val department: Department,
){
    override fun toString(): String {
        return name
    }
}