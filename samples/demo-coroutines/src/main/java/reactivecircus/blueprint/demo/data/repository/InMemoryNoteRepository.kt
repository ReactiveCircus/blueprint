package reactivecircus.blueprint.demo.data.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import reactivecircus.blueprint.demo.data.cache.NoteCache
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.domain.repository.NoteRepository

@FlowPreview
@ExperimentalCoroutinesApi
class InMemoryNoteRepository(
    private val noteCache: NoteCache
) : NoteRepository {

    private val notesBroadcastChannel: BroadcastChannel<Unit> = ConflatedBroadcastChannel(Unit)

    override fun streamAllNotes(): Flow<List<Note>> {
        return notesBroadcastChannel.asFlow()
            .map { noteCache.allNotes() }
    }

    override suspend fun getNoteByUuid(uuid: String): Note? {
        return noteCache.findNote { it.uuid == uuid }
    }

    override suspend fun addNote(note: Note) {
        noteCache.addNotes(listOf(note))

        // refresh all notes stream
        notesBroadcastChannel.offer(Unit)
    }

    override suspend fun updateNote(note: Note) {
        noteCache.updateNote(note)

        // refresh all notes stream
        notesBroadcastChannel.offer(Unit)
    }
}
