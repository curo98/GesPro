package iberoplast.pe.gespro.model

import android.os.Parcel
import android.os.Parcelable
data class Observation(
    val id: Int,
    val title: String,
    val id_user: Int,
    val content: String,
    val created_at: String, // Añade campos relacionados con fechas si es necesario
    val updated_at: String, // Añade campos relacionados con fechas si es necesario
    val user: User
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "", // Agregar campos relacionados con fechas si es necesario
        parcel.readString() ?: "", // Agregar campos relacionados con fechas si es necesario
        parcel.readParcelable(User::class.java.classLoader) ?: User(0, "", "", 0, "", null, null)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeInt(id_user)
        parcel.writeString(content)
        parcel.writeString(created_at) // Agregar campos relacionados con fechas si es necesario
        parcel.writeString(updated_at) // Agregar campos relacionados con fechas si es necesario
        parcel.writeParcelable(user, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Observation> {
        override fun createFromParcel(parcel: Parcel): Observation {
            return Observation(parcel)
        }

        override fun newArray(size: Int): Array<Observation?> {
            return arrayOfNulls(size)
        }
    }
}


