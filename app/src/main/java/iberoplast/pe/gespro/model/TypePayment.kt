package iberoplast.pe.gespro.model

import android.os.Parcel
import android.os.Parcelable

data class TypePayment(
    val id: Int,
    val name: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TypePayment> {
        override fun createFromParcel(parcel: Parcel): TypePayment {
            return TypePayment(parcel)
        }

        override fun newArray(size: Int): Array<TypePayment?> {
            return arrayOfNulls(size)
        }
    }
}
