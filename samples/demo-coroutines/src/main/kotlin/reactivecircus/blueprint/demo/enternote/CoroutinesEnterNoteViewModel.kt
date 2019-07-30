package reactivecircus.blueprint.demo.enternote

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import reactivecircus.blueprint.demo.domain.interactor.CoroutinesCreateNote
import reactivecircus.blueprint.demo.domain.interactor.CoroutinesGetNoteByUuid
import reactivecircus.blueprint.demo.domain.interactor.CoroutinesUpdateNote
import reactivecircus.blueprint.demo.domain.model.Note

data class State(val note: Note?)

class CoroutinesEnterNoteViewModel(
    noteUuid: String?,
    getNoteByUuid: CoroutinesGetNoteByUuid,
    private val createNote: CoroutinesCreateNote,
    private val updateNote: CoroutinesUpdateNote
) : ViewModel() {

    val noteLiveData = MutableLiveData<State>()

    init {
        viewModelScope.launch {
            if (noteUuid != null) {
                val note = getNoteByUuid.execute(CoroutinesGetNoteByUuid.Params(noteUuid))
                noteLiveData.value = State(note)
            } else {
                noteLiveData.value = State(null)
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
            viewModelScope.launch {
                updateNote.execute(CoroutinesUpdateNote.Params(updatedNote))
            }
        }
    }
}
