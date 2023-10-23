package iberoplast.pe.gespro.model.ubigeo_peru

data class Department(
    val id:Int,
    val name:String
)
{
    override fun toString(): String {
        return name
    }
}