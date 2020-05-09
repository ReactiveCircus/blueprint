package reactivecircus.blueprint.demo.noteslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import reactivecircus.blueprint.demo.domain.interactor.CoroutinesStreamAllNotes
import reactivecircus.blueprint.demo.domain.model.Note
import timber.log.Timber

sealed class State {
    object LoadingNotes : State()
    data class Idle(val notes: List<Note>) : State()
}

@ExperimentalCoroutinesApi
class CoroutinesNotesListViewModel(
    streamAllNotes: CoroutinesStreamAllNotes
) : ViewModel() {

    private val notesStateFlow = MutableStateFlow<State>(State.LoadingNotes)

    val notesFlow: Flow<State> = notesStateFlow

    init {
        streamAllNotes.buildFlow(CoroutinesStreamAllNotes.Params(CoroutinesStreamAllNotes.SortedBy.TIME_LAST_UPDATED))
            .map { State.Idle(it) }
            .onEach {
                notesStateFlow.value = it
            }
            .catch {
                Timber.e(it)
            }
            .launchIn(viewModelScope)
    }
}
