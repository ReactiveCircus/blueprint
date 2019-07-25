package reactivecircus.blueprint.demo.domain.interactor

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.domain.repository.NoteRepository
import reactivecircus.blueprint.threading.coroutines.CoroutineDispatchers

@ExperimentalCoroutinesApi
class StreamAllNotesTest {

    private val noteRepository = mockk<NoteRepository>()

    private val coroutineDispatchers = mockk<CoroutineDispatchers> {
        every { io } returns TestCoroutineDispatcher()
    }

    private val streamAllNotes = StreamAllNotes(
        noteRepository,
        coroutineDispatchers
    )

    @Test
    fun `should stream notes from repository`() = runBlockingTest {
        every { noteRepository.streamAllNotes() } returns flowOf(emptyList())

        val result = streamAllNotes.buildFlow(
            StreamAllNotes.Params(StreamAllNotes.SortedBy.TIME_CREATED)
        ).single()

        verify(exactly = 1) {
            noteRepository.streamAllNotes()
        }

        result shouldEqual emptyList()
    }

    @Test
    fun `should sort by TIME_CREATED descending`() = runBlockingTest {
        val note1 = Note(
            content = "note 1",
            timeCreated = 1000L,
            timeLastUpdated = 1000L
        )

        val note2 = Note(
            content = "note 2",
            timeCreated = 2000L,
            timeLastUpdated = 500L
        )

        every { noteRepository.streamAllNotes() } returns flowOf(
            listOf(note1, note2)
        )

        val result = streamAllNotes.buildFlow(
            StreamAllNotes.Params(StreamAllNotes.SortedBy.TIME_CREATED)
        ).single()

        verify(exactly = 1) {
            noteRepository.streamAllNotes()
        }

        result shouldEqual listOf(note2, note1)
    }

    @Test
    fun `should sort by TIME_LAST_UPDATED descending`() = runBlockingTest {
        val note1 = Note(
            content = "note 1",
            timeCreated = 2000L,
            timeLastUpdated = 500L
        )

        val note2 = Note(
            content = "note 2",
            timeCreated = 1000L,
            timeLastUpdated = 1000L
        )

        every { noteRepository.streamAllNotes() } returns flowOf(
            listOf(note1, note2)
        )

        val result = streamAllNotes.buildFlow(
            StreamAllNotes.Params(StreamAllNotes.SortedBy.TIME_LAST_UPDATED)
        ).single()

        verify(exactly = 1) {
            noteRepository.streamAllNotes()
        }

        result shouldEqual listOf(note2, note1)
    }
}
