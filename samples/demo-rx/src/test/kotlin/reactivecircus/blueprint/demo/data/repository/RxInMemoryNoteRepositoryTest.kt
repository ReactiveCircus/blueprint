package reactivecircus.blueprint.demo.data.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import org.junit.Test
import reactivecircus.blueprint.demo.data.cache.NoteCache
import reactivecircus.blueprint.demo.domain.model.Note

class RxInMemoryNoteRepositoryTest {

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

    private val inMemoryRepository = RxInMemoryNoteRepository(noteCache)

    @Test
    fun `start streaming all notes from cache when subscribed`() {
        every { noteCache.allNotes() } returns dummyNotes

        val testObserver = inMemoryRepository.streamAllNotes().test()

        verifyAll { noteCache.allNotes() }

        testObserver.assertValue(dummyNotes)
            .assertNotTerminated()
    }

    @Test
    fun `emit existing note from cache when note with matching uuid exists`() {
        every { noteCache.findNote(any()) } returns dummyNotes[0]

        val testObserver = inMemoryRepository.getNoteByUuid(dummyNotes[0].uuid).test()

        verifyAll { noteCache.findNote(any()) }

        testObserver.assertValue(dummyNotes[0])
            .assertTerminated()
    }

    @Test
    fun `complete without emitting value when no note with matching uuid exists`() {
        every { noteCache.findNote(any()) } returns null

        val testObserver = inMemoryRepository.getNoteByUuid(dummyNotes[0].uuid).test()

        verifyAll { noteCache.findNote(any()) }

        testObserver.assertNoValues()
            .assertTerminated()
    }

    @Test
    fun `a note can be added to cache`() {
        every { noteCache.addNotes(any()) } returns Unit

        val testObserver = inMemoryRepository.addNote(
            Note(
                content = "Note",
                timeCreated = System.currentTimeMillis(),
                timeLastUpdated = System.currentTimeMillis()
            )
        ).test()

        verifyAll { noteCache.addNotes(any()) }

        testObserver.assertComplete()
    }

    @Test
    fun `trigger new emission of all notes when added new note`() {
        every { noteCache.allNotes() } returns dummyNotes
        every { noteCache.addNotes(any()) } returns Unit

        inMemoryRepository.addNote(
            Note(
                content = "Note",
                timeCreated = System.currentTimeMillis(),
                timeLastUpdated = System.currentTimeMillis()
            )
        ).subscribe()

        val testObserver = inMemoryRepository.streamAllNotes().test()

        verify(exactly = 1) { noteCache.allNotes() }

        testObserver.assertValue(dummyNotes)
            .assertNotTerminated()
    }

    @Test
    fun `a note in cache can be updated`() {
        every { noteCache.updateNote(any()) } returns Unit

        val testObserver = inMemoryRepository.updateNote(
            dummyNotes[0].copy(content = "New note")
        ).test()

        verifyAll { noteCache.updateNote(any()) }

        testObserver.assertComplete()
    }

    @Test
    fun `trigger new emission of all notes when updated existing note`() {
        every { noteCache.allNotes() } returns dummyNotes
        every { noteCache.updateNote(any()) } returns Unit

        inMemoryRepository.updateNote(
            dummyNotes[0].copy(content = "New note")
        ).subscribe()

        val testObserver = inMemoryRepository.streamAllNotes().test()

        verify(exactly = 1) { noteCache.allNotes() }

        testObserver.assertValue(dummyNotes)
            .assertNotTerminated()
    }
}
