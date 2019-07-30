package reactivecircus.blueprint.demo.data.cache

import reactivecircus.blueprint.demo.domain.model.Note

interface NoteCache {

    /**
     * Return all existing notes.
     */
    fun allNotes(): List<Note>

    /**
     * Find a [Note] that matches the provided [criteria].
     * Return null if note matching the [criteria] could not be found.
     */
    fun findNote(criteria: (Note) -> Boolean): Note?

    /**
     * Add a list of [Note]s to the list of existing notes.
     * @throws IllegalArgumentException if any new note with same uuid already exists.
     */
    fun addNotes(newNotes: List<Note>)

    /**
     * Update a [Note] by finding the existing note by uuid and replacing it.
     * @throws IllegalArgumentException if note with same uuid does not exist.
     */
    fun updateNote(noteToUpdate: Note)

    /**
     * Delete all existing notes.
     */
    fun deleteAllNotes()
}
