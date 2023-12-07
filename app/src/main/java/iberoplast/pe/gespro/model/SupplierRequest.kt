package iberoplast.pe.gespro.model

import android.os.Parcel
import android.os.Parcelable

data class SupplierRequest(
    val id: Int,
    val id_user: Int,
    val id_type_payment: Int,
    val id_method_payment: Int,
    val deleted_at: String,
    val created_at: String,
    val updated_at: String,
    val stateTransitions: List<StateTransition>,
    val user: User,
    val type_payment: TypePayment,
    val method_payment: MethodPayment,
    val documents: List<Document>,
    val questions: List<Question>,
    val policies: List<Policy>,
    val observations: List<Observation>
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createTypedArrayList(StateTransition.CREATOR) ?: emptyList(),
        parcel.readParcelable(User::class.java.classLoader) ?: User(0, "","", "", 0, "", null, null),
        parcel.readParcelable(TypePayment::class.java.classLoader) ?: TypePayment(0, "", ""),
        parcel.readParcelable(MethodPayment::class.java.classLoader) ?: MethodPayment(0, "", ""),
        parcel.createTypedArrayList(Document.CREATOR) ?: emptyList(),
        parcel.createTypedArrayList(Question.CREATOR) ?: emptyList(),
        parcel.createTypedArrayList(Policy.CREATOR) ?: emptyList(),
        parcel.createTypedArrayList(Observation.CREATOR) ?: emptyList(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(id_user)
        parcel.writeInt(id_type_payment)
        parcel.writeInt(id_method_payment)
        parcel.writeString(deleted_at)
        parcel.writeString(created_at)
        parcel.writeString(updated_at)
        parcel.writeTypedList(stateTransitions)
        parcel.writeParcelable(user, flags)
        parcel.writeParcelable(type_payment, flags)
        parcel.writeParcelable(method_payment, flags)
        parcel.writeTypedList(documents)
        parcel.writeTypedList(questions)
        parcel.writeTypedList(policies)
        parcel.writeTypedList(observations)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SupplierRequest> {
        override fun createFromParcel(parcel: Parcel): SupplierRequest {
            return SupplierRequest(parcel)
        }

        override fun newArray(size: Int): Array<SupplierRequest?> {
            return arrayOfNulls(size)
        }
    }

    fun getFinalState(): StateRequest? {
        // Verificar si hay al menos una transición de estado
        if (stateTransitions.isNotEmpty()) {
            // Obtener la última transición de estado
            val lastTransition = stateTransitions.last()
            // Verificar si existe un "toState"
            return lastTransition.toState
        }
        return null // No hay transiciones de estado
    }
//fun getFinalState(): StateRequest? {
//    // Verificar si hay al menos una transición de estado
//    if (stateTransitions.isNotEmpty()) {
//        // Obtener la última transición de estado
//        val lastTransition = stateTransitions.last()
//
//        // Verificar si el estado actual está en "En corrección" o "Corregida"
//        if (lastTransition.toState.name == "En corrección" || lastTransition.toState.name == "Corregida") {
//            // Obtener la transición anterior
//            val previousTransition = stateTransitions.dropLast(1).lastOrNull()
//            // Devolver el estado anterior si existe
//            return previousTransition?.toState
//        } else {
//            // Devolver el estado actual
//            return lastTransition.toState
//        }
//    }
//    return null // No hay transiciones de estado
//}
//    fun getLastStateTransition(): StateTransition? {
//        return stateTransitions.lastOrNull()
//    }

}

