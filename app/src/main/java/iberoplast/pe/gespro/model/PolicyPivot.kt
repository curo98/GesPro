package iberoplast.pe.gespro.model
import android.os.Parcel
import android.os.Parcelable

data class PolicyPivot(
    val id_supplier_request: Int,
    val id_policie: Int,
    val accepted: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id_supplier_request)
        parcel.writeInt(id_policie)
        parcel.writeInt(accepted)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PolicyPivot> {
        override fun createFromParcel(parcel: Parcel): PolicyPivot {
            return PolicyPivot(parcel)
        }

        override fun newArray(size: Int): Array<PolicyPivot?> {
            return arrayOfNulls(size)
        }
    }
}


