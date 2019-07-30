package reactivecircus.blueprint.demo.enternote

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import reactivecircus.blueprint.demo.domain.interactor.RxCreateNote
import reactivecircus.blueprint.demo.domain.interactor.RxGetNoteByUuid
import reactivecircus.blueprint.demo.domain.interactor.RxUpdateNote
import reactivecircus.blueprint.demo.domain.model.Note
import timber.log.Timber

data class State(val note: Note?)

class RxEnterNoteViewModel(
    noteUuid: String?,
    getNoteByUuid: RxGetNoteByUuid,
    private val createNote: RxCreateNote,
    private val updateNote: RxUpdateNote
) : ViewModel() {

    val noteLiveData = MutableLiveData<State>()

    private val disposable = CompositeDisposable()

    init {
        if (noteUuid != null) {
            disposable += getNoteByUuid.buildSingle(RxGetNoteByUuid.Params(noteUuid))
                .subscribeBy(
                    onSuccess = { note ->
                        noteLiveData.value = State(note)
                    },
                    onError = {
                        Timber.e(it)
                    }
                )
        } else {
            noteLiveData.value = State(null)
        }
    }

    fun createNote(content: String) {
        val time = System.currentTimeMillis()
        val newNote = Note(
            content = content,
            timeCreated = time,
            timeLastUpdated = time
        )

        disposable += createNote.buildCompletable(RxCreateNote.Params(newNote)).subscribeBy(
            onError = {
                Timber.e(it)
            }
        )
    }

    fun updateNote(updatedNote: Note) {
        disposable += updateNote.buildCompletable(RxUpdateNote.Params(updatedNote)).subscribeBy(
            onError = {
                Timber.e(it)
            }
        )
    }

    override fun onCleared() {
        disposable.clear()
    }
}
