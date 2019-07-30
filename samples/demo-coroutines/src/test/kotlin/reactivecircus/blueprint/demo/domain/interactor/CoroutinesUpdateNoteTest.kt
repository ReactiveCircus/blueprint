package reactivecircus.blueprint.demo.domain.interactor

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.domain.repository.CoroutinesNoteRepository
import reactivecircus.blueprint.threading.coroutines.CoroutineDispatcherProvider

@ExperimentalCoroutinesApi
class CoroutinesUpdateNoteTest {

    private val noteRepository = mockk<CoroutinesNoteRepository> {
        coEvery { updateNote(any()) } returns Unit
    }

    private val coroutineDispatcherProvider = mockk<CoroutineDispatcherProvider> {
        every { io } returns TestCoroutineDispatcher()
    }

    private val updateNote = CoroutinesUpdateNote(
        noteRepository = noteRepository,
        coroutineDispatcherProvider = coroutineDispatcherProvider
    )

    @Test
    fun `should update note in repository`() = runBlockingTest {
        val dummyNote = Note(
            content = "note",
            timeCreated = System.currentTimeMillis(),
            timeLastUpdated = System.currentTimeMillis()
        )

        updateNote.execute(CoroutinesUpdateNote.Params(dummyNote)) shouldEqual Unit

        coVerify { noteRepository.updateNote(any()) }
    }
}
