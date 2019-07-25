package reactivecircus.blueprint.demo.data.cache

import reactivecircus.blueprint.demo.domain.model.Note

/**
 * In-memory implementation of [NoteCache].
 */
class InMemoryNoteCache : NoteCache {

    private val notes = mutableListOf<Note>()

    override fun allNotes(): List<Note> {
        return notes
    }

    override fun findNote(criteria: (Note) -> Boolean): Note? {
        return notes.find(criteria)
    }

    override fun addNotes(newNotes: List<Note>) {
        require(notes.none { existingNote ->
            newNotes.any { newNote ->
                existingNote.uuid == newNote.uuid
            }
        }) {
            "Note already exists."
        }

        notes.addAll(newNotes)
    }

    override fun updateNote(noteToUpdate: Note) {
        val noteIndex = notes.indexOfFirst {
            it.uuid == noteToUpdate.uuid
        }

        require(noteIndex >= 0) {
            "Note does not exist."
        }

        notes[noteIndex] = noteToUpdate
    }

    override fun deleteAllNotes() {
        notes.clear()
    }
}
