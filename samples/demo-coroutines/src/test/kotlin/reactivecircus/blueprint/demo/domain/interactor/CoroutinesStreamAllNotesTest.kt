package reactivecircus.blueprint.demo.domain.interactor

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.domain.repository.CoroutinesNoteRepository

@ExperimentalCoroutinesApi
class CoroutinesStreamAllNotesTest {

    private val noteRepository = mockk<CoroutinesNoteRepository>()

    private val coroutineDispatcherProvider = mockk<CoroutineDispatcherProvider> {
        every { io } returns TestCoroutineDispatcher()
    }

    private val streamAllNotes = CoroutinesStreamAllNotes(
        noteRepository,
        coroutineDispatcherProvider
    )

    @Test
    fun `stream notes from repository`() = runBlockingTest {
        every { noteRepository.streamAllNotes() } returns flowOf(emptyList())

        val result = streamAllNotes.buildFlow(
            CoroutinesStreamAllNotes.Params(CoroutinesStreamAllNotes.SortedBy.TIME_CREATED)
        ).first()

        verify(exactly = 1) {
            noteRepository.streamAllNotes()
        }

        assertThat(result)
            .isEmpty()
    }

    @Test
    fun `sort notes by TIME_CREATED descending`() = runBlockingTest {
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
            CoroutinesStreamAllNotes.Params(CoroutinesStreamAllNotes.SortedBy.TIME_CREATED)
        ).first()

        verify(exactly = 1) {
            noteRepository.streamAllNotes()
        }

        assertThat(result)
            .isEqualTo(listOf(note2, note1))
    }

    @Test
    fun `sort notes by TIME_LAST_UPDATED descending`() = runBlockingTest {
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
            CoroutinesStreamAllNotes.Params(CoroutinesStreamAllNotes.SortedBy.TIME_LAST_UPDATED)
        ).first()

        verify(exactly = 1) {
            noteRepository.streamAllNotes()
        }

        assertThat(result)
            .isEqualTo(listOf(note2, note1))
    }
}
