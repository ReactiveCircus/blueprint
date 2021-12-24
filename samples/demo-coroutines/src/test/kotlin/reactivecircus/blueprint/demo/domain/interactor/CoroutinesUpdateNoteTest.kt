package reactivecircus.blueprint.demo.domain.interactor

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.domain.repository.CoroutinesNoteRepository

@ExperimentalCoroutinesApi
class CoroutinesUpdateNoteTest {

    private val testDispatcher = StandardTestDispatcher()

    private val noteRepository = mockk<CoroutinesNoteRepository> {
        coEvery { updateNote(any()) } returns Unit
    }

    private val coroutineDispatcherProvider = mockk<CoroutineDispatcherProvider> {
        every { io } returns testDispatcher
    }

    private val updateNote = CoroutinesUpdateNote(
        noteRepository = noteRepository,
        coroutineDispatcherProvider = coroutineDispatcherProvider
    )

    @Test
    fun `update note in repository`() = runTest(testDispatcher) {
        val dummyNote = Note(
            content = "note",
            timeCreated = System.currentTimeMillis(),
            timeLastUpdated = System.currentTimeMillis()
        )

        assertThat(updateNote.execute(CoroutinesUpdateNote.Params(dummyNote)))
            .isEqualTo(Unit)

        coVerify { noteRepository.updateNote(any()) }
    }
}
