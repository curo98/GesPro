package iberoplast.pe.gespro.model

import android.os.Parcel
import android.os.Parcelable

data class MethodPayment(val id:Int, val name:String, val description: String) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MethodPayment> {
        override fun createFromParcel(parcel: Parcel): MethodPayment {
            return MethodPayment(parcel)
        }

        override fun newArray(size: Int): Array<MethodPayment?> {
            return arrayOfNulls(size)
        }
    }
}