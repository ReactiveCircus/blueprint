package reactivecircus.blueprint.demo.enternote

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import reactivecircus.blueprint.demo.domain.interactor.CreateNote
import reactivecircus.blueprint.demo.domain.interactor.GetNoteByUuid
import reactivecircus.blueprint.demo.domain.interactor.UpdateNote
import reactivecircus.blueprint.demo.domain.model.Note

data class State(val note: Note?)

class EnterNoteViewModel(
    noteUuid: String?,
    getNoteByUuid: GetNoteByUuid,
    private val createNote: CreateNote,
    private val updateNote: UpdateNote
) : ViewModel() {

    val noteLiveData = MutableLiveData<State>()

    init {
        viewModelScope.launch {
            if (noteUuid != null) {
                val note = getNoteByUuid.execute(GetNoteByUuid.Params(noteUuid))
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
            createNote.execute(CreateNote.Params(newNote))
        }
    }

    fun updateNote(updatedNote: Note) {
        viewModelScope.launch {
            viewModelScope.launch {
                updateNote.execute(UpdateNote.Params(updatedNote))
            }
        }
    }
}
