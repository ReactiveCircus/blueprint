package reactivecircus.blueprint.demo.data.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.blueprint.demo.data.cache.NoteCache
import reactivecircus.blueprint.demo.domain.model.Note

@FlowPreview
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
    fun `start streaming all notes from cache when collected`() = runBlockingTest {
        every { noteCache.allNotes() } returns dummyNotes

        val result = inMemoryRepository.streamAllNotes().take(1).single()

        verifyAll { noteCache.allNotes() }

        result shouldEqual dummyNotes
    }

    @Test
    fun `return existing note from cache when note with matching uuid exists`() =
        runBlockingTest {
            every { noteCache.findNote(any()) } returns dummyNotes[0]

            val result = inMemoryRepository.getNoteByUuid(dummyNotes[0].uuid)

            verifyAll { noteCache.findNote(any()) }

            result shouldEqual dummyNotes[0]
        }

    @Test
    fun `return null when no note with matching uuid exists`() = runBlockingTest {
        every { noteCache.findNote(any()) } returns null

        val result = inMemoryRepository.getNoteByUuid(dummyNotes[0].uuid)

        verifyAll { noteCache.findNote(any()) }

        result shouldEqual null
    }

    @Test
    fun `a note can be added to cache`() = runBlockingTest {
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
    fun `trigger new emission of all notes when added new note`() = runBlockingTest {
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

        result shouldEqual dummyNotes
    }

    @Test
    fun `a note in cache can be updated`() = runBlockingTest {
        every { noteCache.updateNote(any()) } returns Unit

        inMemoryRepository.updateNote(
            dummyNotes[0].copy(content = "New note")
        )

        verifyAll { noteCache.updateNote(any()) }
    }

    @Test
    fun `trigger new emission of all notes when updated existing note`() = runBlockingTest {
        every { noteCache.allNotes() } returns dummyNotes
        every { noteCache.updateNote(any()) } returns Unit

        inMemoryRepository.updateNote(
            dummyNotes[0].copy(content = "New note")
        )

        val result = inMemoryRepository.streamAllNotes().take(1).single()

        verify(exactly = 1) { noteCache.allNotes() }

        result shouldEqual dummyNotes
    }
}
