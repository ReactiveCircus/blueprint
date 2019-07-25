package reactivecircus.blueprint.demo.enternote

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class EnterNoteParams : Parcelable {

    @Parcelize
    object CreateNew : EnterNoteParams()

    @Parcelize
    class Update(val uuid: String) : EnterNoteParams()
}
