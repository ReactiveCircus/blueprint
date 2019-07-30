package reactivecircus.blueprint.demo.domain.repository

import kotlinx.coroutines.flow.Flow
import reactivecircus.blueprint.demo.domain.model.Note

interface CoroutinesNoteRepository {

    fun streamAllNotes(): Flow<List<Note>>

    suspend fun getNoteByUuid(uuid: String): Note?

    suspend fun addNote(note: Note)

    suspend fun updateNote(note: Note)
}
