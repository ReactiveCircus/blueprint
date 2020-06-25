package reactivecircus.blueprint.demo.noteslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import reactivecircus.blueprint.demo.domain.interactor.RxStreamAllNotes
import reactivecircus.blueprint.demo.domain.model.Note
import timber.log.Timber

sealed class State {
    object LoadingNotes : State()
    data class Idle(val notes: List<Note>) : State()
}

class RxNotesListViewModel(
    streamAllNotes: RxStreamAllNotes
) : ViewModel() {

    private val mutableNotesLiveData = MutableLiveData<State>()

    val notesLiveData: LiveData<State> get() = mutableNotesLiveData

    private val disposable = CompositeDisposable()

    init {
        disposable += streamAllNotes
            .buildObservable(
                RxStreamAllNotes.Params(RxStreamAllNotes.SortedBy.TIME_LAST_UPDATED)
            )
            .map<State> { State.Idle(it) }
            .startWithItem(State.LoadingNotes)
            .subscribeBy(
                onNext = {
                    mutableNotesLiveData.value = it
                },
                onError = {
                    Timber.e(it)
                }
            )
    }

    override fun onCleared() {
        disposable.clear()
    }
}
