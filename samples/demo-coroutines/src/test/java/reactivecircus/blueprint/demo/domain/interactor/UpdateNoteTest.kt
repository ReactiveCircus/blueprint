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
import reactivecircus.blueprint.demo.domain.repository.NoteRepository
import reactivecircus.blueprint.threading.coroutines.CoroutineDispatchers

@ExperimentalCoroutinesApi
class UpdateNoteTest {

    private val noteRepository = mockk<NoteRepository> {
        coEvery { updateNote(any()) } returns Unit
    }

    private val coroutineDispatchers = mockk<CoroutineDispatchers> {
        every { io } returns TestCoroutineDispatcher()
    }

    private val updateNote = UpdateNote(
        noteRepository = noteRepository,
        coroutineDispatchers = coroutineDispatchers
    )

    @Test
    fun `should update note in repository`() = runBlockingTest {
        val dummyNote = Note(
            content = "note",
            timeCreated = System.currentTimeMillis(),
            timeLastUpdated = System.currentTimeMillis()
        )

        updateNote.execute(UpdateNote.Params(dummyNote)) shouldEqual Unit

        coVerify { noteRepository.updateNote(any()) }
    }
}
