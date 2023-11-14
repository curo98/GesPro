package iberoplast.pe.gespro.model
import android.os.Parcel
import android.os.Parcelable

data class Policy(
    val id: Int,
    val title: String,
    val content: String,
    val isChecked: Boolean,
    val pivot: PolicyPivot
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readParcelable(PolicyPivot::class.java.classLoader) ?: PolicyPivot(0, 0, 0)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(content)
        parcel.writeByte(if (isChecked) 1 else 0)
        parcel.writeParcelable(pivot, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Policy> {
        override fun createFromParcel(parcel: Parcel): Policy {
            return Policy(parcel)
        }

        override fun newArray(size: Int): Array<Policy?> {
            return arrayOfNulls(size)
        }
    }
}
