package iberoplast.pe.gespro.model

import Role
import android.os.Parcel
import android.os.Parcelable

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val id_role: Int?,
    val device_token: String?,
    val role: Role?,
    val supplier: Supplier?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readParcelable(Role::class.java.classLoader),
        parcel.readParcelable(Supplier::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeValue(id_role)
        parcel.writeString(device_token)
        parcel.writeParcelable(role, flags)
        parcel.writeParcelable(supplier, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
