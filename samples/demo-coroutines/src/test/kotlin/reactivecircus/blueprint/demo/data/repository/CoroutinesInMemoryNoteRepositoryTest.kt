package reactivecircus.blueprint.demo.data.repository

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.Test
import reactivecircus.blueprint.demo.data.cache.NoteCache
import reactivecircus.blueprint.demo.domain.model.Note

@ExperimentalCoroutinesApi
class CoroutinesInMemoryNoteRepositoryTest {

    private val dummyNotes = listOf(
        Note(
            uuid = "uuid1",
            content = "Note 1",
            timeCreated = System.currentTimeMillis(),
            timeLastUpdated = System.currentTimeMillis()
        ),
        Note(
            uuid = "uuid2",
            content = "Note 2",
            timeCreated = System.currentTimeMillis(),
            timeLastUpdated = System.currentTimeMillis()
        )
    )

    private val noteCache = mockk<NoteCache>()

    private val inMemoryRepository = CoroutinesInMemoryNoteRepository(noteCache)

    @Test
    fun `start streaming all notes from cache when collected`() = runTest {
        every { noteCache.allNotes() } returns dummyNotes

        val result = inMemoryRepository.streamAllNotes().take(1).single()

        verifyAll { noteCache.allNotes() }

        assertThat(result)
            .isEqualTo(dummyNotes)
    }

    @Test
    fun `return existing note from cache when note with matching uuid exists`() =
        runTest {
            every { noteCache.findNote(any()) } returns dummyNotes[0]

            val result = inMemoryRepository.getNoteByUuid(dummyNotes[0].uuid)

            verifyAll { noteCache.findNote(any()) }

            assertThat(result)
                .isEqualTo(dummyNotes[0])
        }

    @Test
    fun `return null when no note with matching uuid exists`() = runTest {
        every { noteCache.findNote(any()) } returns null

        val result = inMemoryRepository.getNoteByUuid(dummyNotes[0].uuid)

        verifyAll { noteCache.findNote(any()) }

        assertThat(result)
            .isNull()
    }

    @Test
    fun `a note can be added to cache`() = runTest {
        every { noteCache.addNotes(any()) } returns Unit

        inMemoryRepository.addNote(
            Note(
                content = "Note",
                timeCreated = System.currentTimeMillis(),
                timeLastUpdated = System.currentTimeMillis()
            )
        )

        verifyAll { noteCache.addNotes(any()) }
    }

    @Test
    fun `trigger new emission of all notes when added new note`() = runTest {
        every { noteCache.allNotes() } returns dummyNotes
        every { noteCache.addNotes(any()) } returns Unit

        inMemoryRepository.addNote(
            Note(
                content = "Note",
                timeCreated = System.currentTimeMillis(),
                timeLastUpdated = System.currentTimeMillis()
            )
        )

        val result = inMemoryRepository.streamAllNotes().take(1).single()

        verify(exactly = 1) { noteCache.allNotes() }

        assertThat(result)
            .isEqualTo(dummyNotes)
    }

    @Test
    fun `a note in cache can be updated`() = runTest {
        every { noteCache.updateNote(any()) } returns Unit

        inMemoryRepository.updateNote(
            dummyNotes[0].copy(content = "New note")
        )

        verifyAll { noteCache.updateNote(any()) }
    }

    @Test
    fun `trigger new emission of all notes when updated existing note`() = runTest {
        every { noteCache.allNotes() } returns dummyNotes
        every { noteCache.updateNote(any()) } returns Unit

        inMemoryRepository.updateNote(
            dummyNotes[0].copy(content = "New note")
        )

        val result = inMemoryRepository.streamAllNotes().take(1).single()

        verify(exactly = 1) { noteCache.allNotes() }

        assertThat(result)
            .isEqualTo(dummyNotes)
    }
}
