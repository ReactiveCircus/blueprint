package reactivecircus.blueprint.demo.data.repository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyAll
import io.mockk.mockk
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
class InMemoryNoteRepositoryTest {

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

    private val inMemoryRepository = InMemoryNoteRepository(noteCache)

    @Test
    fun `should start streaming all notes from cache when collected`() = runBlockingTest {
        coEvery { noteCache.allNotes() } returns dummyNotes

        val result = inMemoryRepository.streamAllNotes().take(1).single()

        coVerifyAll { noteCache.allNotes() }

        result shouldEqual dummyNotes
    }

    @Test
    fun `should return existing note from cache when note with matching uuid exists`() =
        runBlockingTest {
            coEvery { noteCache.findNote(any()) } returns dummyNotes[0]

            val result = inMemoryRepository.getNoteByUuid(dummyNotes[0].uuid)

            coVerifyAll { noteCache.findNote(any()) }

            result shouldEqual dummyNotes[0]
        }

    @Test
    fun `should return null when no note with matching uuid exists`() = runBlockingTest {
        coEvery { noteCache.findNote(any()) } returns null

        val result = inMemoryRepository.getNoteByUuid(dummyNotes[0].uuid)

        coVerifyAll { noteCache.findNote(any()) }

        result shouldEqual null
    }

    @Test
    fun `should add note to cache`() = runBlockingTest {
        coEvery { noteCache.addNotes(any()) } returns Unit

        inMemoryRepository.addNote(
            Note(
                content = "Note",
                timeCreated = System.currentTimeMillis(),
                timeLastUpdated = System.currentTimeMillis()
            )
        )

        coVerifyAll { noteCache.addNotes(any()) }
    }

    @Test
    fun `should trigger new emission of all notes when added new note`() = runBlockingTest {
        coEvery { noteCache.allNotes() } returns dummyNotes
        coEvery { noteCache.addNotes(any()) } returns Unit

        inMemoryRepository.addNote(
            Note(
                content = "Note",
                timeCreated = System.currentTimeMillis(),
                timeLastUpdated = System.currentTimeMillis()
            )
        )

        val result = inMemoryRepository.streamAllNotes().take(1).single()

        coVerify(exactly = 1) { noteCache.allNotes() }

        result shouldEqual dummyNotes
    }

    @Test
    fun `should update note in cache`() = runBlockingTest {
        coEvery { noteCache.updateNote(any()) } returns Unit

        inMemoryRepository.updateNote(
            dummyNotes[0].copy(content = "New note")
        )

        coVerifyAll { noteCache.updateNote(any()) }
    }

    @Test
    fun `should trigger new emission of all notes when updated existing note`() = runBlockingTest {
        coEvery { noteCache.allNotes() } returns dummyNotes
        coEvery { noteCache.updateNote(any()) } returns Unit

        inMemoryRepository.updateNote(
            dummyNotes[0].copy(content = "New note")
        )

        val result = inMemoryRepository.streamAllNotes().take(1).single()

        coVerify(exactly = 1) { noteCache.allNotes() }

        result shouldEqual dummyNotes
    }
}
