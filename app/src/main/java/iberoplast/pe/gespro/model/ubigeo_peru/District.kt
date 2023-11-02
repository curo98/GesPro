package iberoplast.pe.gespro.model.ubigeo_peru

data class District(
    val id:Int,
    val name:String,
    val province_id: Int,

    val province: Province,

    val department_id: Int,

    val department: Department,
){
    override fun toString(): String {
        return name
    }
}