package iberoplast.pe.gespro.model

import android.os.Parcel
import android.os.Parcelable

data class Document(
    val id: Int,
    val title: String,
    val name: String,
    val uri: String,
    val id_supplier: Int,
    val supplier: Supplier,
    val pivot: DocumentPivot
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readParcelable(Supplier::class.java.classLoader) ?: Supplier(0, "", "","", "", "", 0, "", "", "", User(0, "", "", "", 0, "", null, null)),
        parcel.readParcelable(DocumentPivot::class.java.classLoader) ?: DocumentPivot(0, 0)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(name)
        parcel.writeString(uri)
        parcel.writeInt(id_supplier)
        parcel.writeParcelable(supplier, flags)
        parcel.writeParcelable(pivot, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Document> {
        override fun createFromParcel(parcel: Parcel): Document {
            return Document(parcel)
        }

        override fun newArray(size: Int): Array<Document?> {
            return arrayOfNulls(size)
        }
    }
}