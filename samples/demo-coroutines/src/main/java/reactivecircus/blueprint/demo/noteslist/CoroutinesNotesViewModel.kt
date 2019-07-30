package reactivecircus.blueprint.demo.noteslist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import reactivecircus.blueprint.demo.domain.interactor.CoroutinesStreamAllNotes
import reactivecircus.blueprint.demo.domain.model.Note
import timber.log.Timber

sealed class State {
    object LoadingNotes : State()
    data class Idle(val notes: List<Note>) : State()
}

@ExperimentalCoroutinesApi
class CoroutinesNotesViewModel(
    streamAllNotes: CoroutinesStreamAllNotes
) : ViewModel() {

    val notesLiveData = MutableLiveData<State>()

    init {
        streamAllNotes.buildFlow(CoroutinesStreamAllNotes.Params(CoroutinesStreamAllNotes.SortedBy.TIME_LAST_UPDATED))
            .map { State.Idle(it) }
            .onStart<State> {
                emit(State.LoadingNotes)
            }
            .onEach {
                notesLiveData.value = it
            }
            .catch {
                Timber.e(it)
            }
            .launchIn(viewModelScope)
    }
}
