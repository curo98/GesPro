package iberoplast.pe.gespro.model

data class DocumentLoadToServer(
    val title: String,
    val filename: String,
    val fileContent: ByteArray
)
