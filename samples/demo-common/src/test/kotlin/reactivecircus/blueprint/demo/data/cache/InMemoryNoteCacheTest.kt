package reactivecircus.blueprint.demo.data.cache

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.testutils.assertThrows

@ExperimentalCoroutinesApi
class InMemoryNoteCacheTest {

    private val inMemoryNoteCache = InMemoryNoteCache()

    @Test
    fun `cache is initially empty`() {
        assertThat(inMemoryNoteCache.allNotes())
            .isEmpty()
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

        assertThat(inMemoryNoteCache.allNotes())
            .isEqualTo(notes)
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

        assertThat(inMemoryNoteCache.findNote { it.uuid == note.uuid })
            .isEqualTo(note)
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

        assertThat(inMemoryNoteCache.findNote { it.uuid == "uuid2" })
            .isNull()
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

        assertThat(inMemoryNoteCache.allNotes())
            .isEqualTo(listOf(note1))

        inMemoryNoteCache.addNotes(listOf(note2))

        assertThat(inMemoryNoteCache.allNotes())
            .isEqualTo(listOf(note1, note2))
    }

    @Test
    fun `throw exception when adding note with same uuid as an existing note`() = runBlockingTest {
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

        assertThrows<IllegalArgumentException> {
            inMemoryNoteCache.addNotes(listOf(note1.copy(content = "Note 2"), note2))
        }
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

        assertThat(inMemoryNoteCache.findNote { it.uuid == note.uuid })
            .isEqualTo(updatedNote)
    }

    @Test
    fun `throw exception when updating note which does not already exist in the cache`() =
        runBlockingTest {
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

            assertThrows<IllegalArgumentException> {
                inMemoryNoteCache.updateNote(note2)
            }
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

        assertThat(inMemoryNoteCache.allNotes())
            .isEmpty()
    }
}
