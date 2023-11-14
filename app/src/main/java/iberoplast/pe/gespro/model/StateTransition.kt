package iberoplast.pe.gespro.model

import android.os.Parcel
import android.os.Parcelable

data class StateTransition(
    val from_state_id: Int,
    val to_state_id: Int,
    val id_reviewer: Int,
    val created_at: String,
    val fromState: StateRequest,
    val toState: StateRequest,
    val reviewer: User
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readParcelable(StateRequest::class.java.classLoader) ?: StateRequest(0, ""),
        parcel.readParcelable(StateRequest::class.java.classLoader) ?: StateRequest(0, ""),
        parcel.readParcelable(User::class.java.classLoader) ?: User(0, "", "", 0, "", null)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(from_state_id)
        parcel.writeInt(to_state_id)
        parcel.writeInt(id_reviewer)
        parcel.writeString(created_at)
        parcel.writeParcelable(fromState, flags)
        parcel.writeParcelable(toState, flags)
        parcel.writeParcelable(reviewer, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StateTransition> {
        override fun createFromParcel(parcel: Parcel): StateTransition {
            return StateTransition(parcel)
        }

        override fun newArray(size: Int): Array<StateTransition?> {
            return arrayOfNulls(size)
        }
    }
}
