package reactivecircus.blueprint.demo.data.cache

import org.amshove.kluent.invoking
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.junit.Test
import reactivecircus.blueprint.demo.domain.model.Note

class InMemoryNoteCacheTest {

    private val inMemoryNoteCache = InMemoryNoteCache()

    @Test
    fun `cache is initially empty`() {
        inMemoryNoteCache.allNotes() shouldEqual emptyList()
    }

    @Test
    fun `all added notes can be found from the cache`() {
        val notes = listOf(
            Note(
                content = "Note 1",
                timeCreated = System.currentTimeMillis(),
                timeLastUpdated = System.currentTimeMillis()
            ),
            Note(
                content = "Note 2",
                timeCreated = System.currentTimeMillis(),
                timeLastUpdated = System.currentTimeMillis()
            )
        )

        notes.forEach { note ->
            inMemoryNoteCache.addNotes(listOf(note))
        }

        inMemoryNoteCache.allNotes() shouldEqual notes
    }

    @Test
    fun `can find a note if it exists in the cache`() {
        val note = Note(
            uuid = "uuid1",
            content = "Note 1",
            timeCreated = System.currentTimeMillis(),
            timeLastUpdated = System.currentTimeMillis()
        )

        inMemoryNoteCache.addNotes(listOf(note))

        inMemoryNoteCache.findNote { it.uuid == note.uuid } shouldEqual note
    }

    @Test
    fun `return null when finding the note cannot be found in the cache`() {
        val note = Note(
            uuid = "uuid1",
            content = "Note 1",
            timeCreated = System.currentTimeMillis(),
            timeLastUpdated = System.currentTimeMillis()
        )

        inMemoryNoteCache.addNotes(listOf(note))

        inMemoryNoteCache.findNote { it.uuid == "uuid2" } shouldEqual null
    }

    @Test
    fun `new note can be added when not already exists in cache`() {
        val note1 = Note(
            uuid = "uuid1",
            content = "Note 1",
            timeCreated = System.currentTimeMillis(),
            timeLastUpdated = System.currentTimeMillis()
        )

        val note2 = Note(
            uuid = "uuid2",
            content = "Note 2",
            timeCreated = System.currentTimeMillis(),
            timeLastUpdated = System.currentTimeMillis()
        )

        inMemoryNoteCache.addNotes(listOf(note1))

        inMemoryNoteCache.allNotes() shouldEqual listOf(note1)

        inMemoryNoteCache.addNotes(listOf(note2))

        inMemoryNoteCache.allNotes() shouldEqual listOf(note1, note2)
    }

    @Test
    fun `throw exception when adding note with same uuid as an existing note`() {
        val note1 = Note(
            uuid = "uuid1",
            content = "Note 1",
            timeCreated = System.currentTimeMillis(),
            timeLastUpdated = System.currentTimeMillis()
        )

        val note2 = Note(
            uuid = "uuid2",
            content = "Note 2",
            timeCreated = System.currentTimeMillis(),
            timeLastUpdated = System.currentTimeMillis()
        )

        inMemoryNoteCache.addNotes(listOf(note1))

        invoking {
            inMemoryNoteCache.addNotes(listOf(note1.copy(content = "Note 2"), note2))
        } shouldThrow IllegalArgumentException::class
    }

    @Test
    fun `existing note can be updated`() {
        val note = Note(
            uuid = "uuid1",
            content = "Note 1",
            timeCreated = System.currentTimeMillis(),
            timeLastUpdated = System.currentTimeMillis()
        )

        inMemoryNoteCache.addNotes(listOf(note))

        val updatedNote = note.copy(content = "Updated note")

        inMemoryNoteCache.updateNote(updatedNote)

        inMemoryNoteCache.findNote { it.uuid == note.uuid } shouldEqual updatedNote
    }

    @Test
    fun `throw exception when updating note which does not already exist in the cache`() {
        val note1 = Note(
            uuid = "uuid1",
            content = "Note 1",
            timeCreated = System.currentTimeMillis(),
            timeLastUpdated = System.currentTimeMillis()
        )

        val note2 = Note(
            uuid = "uuid2",
            content = "Note 2",
            timeCreated = System.currentTimeMillis(),
            timeLastUpdated = System.currentTimeMillis()
        )

        inMemoryNoteCache.addNotes(listOf(note1))

        invoking {
            inMemoryNoteCache.updateNote(note2)
        } shouldThrow IllegalArgumentException::class
    }

    @Test
    fun `all existing notes can be deleted`() {
        val notes = listOf(
            Note(
                content = "Note 1",
                timeCreated = System.currentTimeMillis(),
                timeLastUpdated = System.currentTimeMillis()
            ),
            Note(
                content = "Note 2",
                timeCreated = System.currentTimeMillis(),
                timeLastUpdated = System.currentTimeMillis()
            )
        )

        inMemoryNoteCache.addNotes(notes)

        inMemoryNoteCache.deleteAllNotes()

        inMemoryNoteCache.allNotes() shouldEqual emptyList()
    }
}
