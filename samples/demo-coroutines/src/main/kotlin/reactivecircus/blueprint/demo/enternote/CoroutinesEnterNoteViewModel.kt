package reactivecircus.blueprint.demo.enternote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import reactivecircus.blueprint.demo.domain.interactor.CoroutinesCreateNote
import reactivecircus.blueprint.demo.domain.interactor.CoroutinesGetNoteByUuid
import reactivecircus.blueprint.demo.domain.interactor.CoroutinesUpdateNote
import reactivecircus.blueprint.demo.domain.model.Note

sealed class State {
    object Loading : State()
    data class Idle(val note: Note?) : State()
}

@ExperimentalCoroutinesApi
class CoroutinesEnterNoteViewModel(
    noteUuid: String?,
    getNoteByUuid: CoroutinesGetNoteByUuid,
    private val createNote: CoroutinesCreateNote,
    private val updateNote: CoroutinesUpdateNote
) : ViewModel() {

    private val noteDataFlow = MutableStateFlow<State>(State.Loading)

    val noteStateFlow: StateFlow<State> get() = noteDataFlow

    init {
        viewModelScope.launch {
            if (noteUuid != null) {
                val note = getNoteByUuid.execute(CoroutinesGetNoteByUuid.Params(noteUuid))
                noteDataFlow.value = State.Idle(note)
            } else {
                noteDataFlow.value = State.Idle(null)
            }
        }
    }

    fun createNote(content: String) {
        viewModelScope.launch {
            val time = System.currentTimeMillis()
            val newNote = Note(
                content = content,
                timeCreated = time,
                timeLastUpdated = time
            )
            createNote.execute(CoroutinesCreateNote.Params(newNote))
        }
    }

    fun updateNote(updatedNote: Note) {
        viewModelScope.launch {
            updateNote.execute(CoroutinesUpdateNote.Params(updatedNote))
        }
    }
}
