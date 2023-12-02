package iberoplast.pe.gespro.model

import android.os.Parcel
import android.os.Parcelable

data class DocumentPivot(
    val id_supplier_request: Int,
    val id_document: Int,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id_supplier_request)
        parcel.writeInt(id_document)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DocumentPivot> {
        override fun createFromParcel(parcel: Parcel): DocumentPivot {
            return DocumentPivot(parcel)
        }

        override fun newArray(size: Int): Array<DocumentPivot?> {
            return arrayOfNulls(size)
        }
    }
}

