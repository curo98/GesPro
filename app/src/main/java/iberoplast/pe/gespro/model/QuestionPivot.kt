package iberoplast.pe.gespro.model

import android.os.Parcel
import android.os.Parcelable

data class QuestionPivot(
    val id_supplier_request: Int,
    val id_question: Int,
    val response: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id_supplier_request)
        parcel.writeInt(id_question)
        parcel.writeInt(response)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<QuestionPivot> {
        override fun createFromParcel(parcel: Parcel): QuestionPivot {
            return QuestionPivot(parcel)
        }

        override fun newArray(size: Int): Array<QuestionPivot?> {
            return arrayOfNulls(size)
        }
    }
}

