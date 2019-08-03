package reactivecircus.blueprint.demo.noteslist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import reactivecircus.blueprint.demo.domain.interactor.RxStreamAllNotes
import reactivecircus.blueprint.demo.domain.model.Note
import timber.log.Timber

sealed class State {
    object LoadingNotes : State()
    data class Idle(val notes: List<Note>) : State()
}

class RxNotesViewModel(
    streamAllNotes: RxStreamAllNotes
) : ViewModel() {

    val notesLiveData = MutableLiveData<State>()

    private val disposable = CompositeDisposable()

    init {
        disposable += streamAllNotes
            .buildObservable(
                RxStreamAllNotes.Params(RxStreamAllNotes.SortedBy.TIME_LAST_UPDATED)
            )
            .map<State> { State.Idle(it) }
            .startWith(State.LoadingNotes)
            .subscribeBy(
                onNext = {
                    notesLiveData.value = it
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
