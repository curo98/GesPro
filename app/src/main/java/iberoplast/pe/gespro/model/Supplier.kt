package iberoplast.pe.gespro.model

import android.os.Parcel
import android.os.Parcelable

data class Supplier(
    val id: Int,
    val nacionality: String,
    val nic_ruc: String,
    val locality: String,
    val street_and_number: String,
    val id_user: Int,
    val state: String,
    val created_at: String,
    val updated_at: String,
    val user: User
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable(User::class.java.classLoader) ?: User(0, "", "", 0, "", null, null)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nacionality)
        parcel.writeString(nic_ruc)
        parcel.writeString(locality)
        parcel.writeString(street_and_number)
        parcel.writeInt(id_user)
        parcel.writeString(state)
        parcel.writeString(created_at)
        parcel.writeString(updated_at)
        parcel.writeParcelable(user, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Supplier> {
        override fun createFromParcel(parcel: Parcel): Supplier {
            return Supplier(parcel)
        }

        override fun newArray(size: Int): Array<Supplier?> {
            return arrayOfNulls(size)
        }
    }
}
