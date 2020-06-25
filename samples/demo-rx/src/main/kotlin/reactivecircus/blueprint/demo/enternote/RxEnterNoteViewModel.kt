package reactivecircus.blueprint.demo.enternote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
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

    private val mutableNoteLiveData = MutableLiveData<State>()

    val noteLiveData: LiveData<State> get() = mutableNoteLiveData

    private val disposable = CompositeDisposable()

    init {
        if (noteUuid != null) {
            disposable += getNoteByUuid.buildSingle(RxGetNoteByUuid.Params(noteUuid))
                .subscribeBy(
                    onSuccess = { note ->
                        mutableNoteLiveData.value = State(note)
                    },
                    onError = {
                        Timber.e(it)
                    }
                )
        } else {
            mutableNoteLiveData.value = State(null)
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
