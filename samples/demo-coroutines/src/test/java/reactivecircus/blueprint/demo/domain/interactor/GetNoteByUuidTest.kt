package reactivecircus.blueprint.demo.domain.interactor

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.junit.Test
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.domain.repository.NoteRepository
import reactivecircus.blueprint.threading.coroutines.CoroutineDispatchers

@ExperimentalCoroutinesApi
class GetNoteByUuidTest {

    private val noteRepository = mockk<NoteRepository>()

    private val coroutineDispatchers = mockk<CoroutineDispatchers> {
        every { io } returns TestCoroutineDispatcher()
    }

    private val getNoteByUuid = GetNoteByUuid(
        noteRepository = noteRepository,
        coroutineDispatchers = coroutineDispatchers
    )

    @Test
    fun `should get note by uuid from repository`() = runBlockingTest {
        val dummyNote = Note(
            content = "note",
            timeCreated = System.currentTimeMillis(),
            timeLastUpdated = System.currentTimeMillis()
        )

        coEvery { noteRepository.getNoteByUuid(any()) } returns dummyNote

        getNoteByUuid.execute(GetNoteByUuid.Params("uuid")) shouldEqual dummyNote

        coVerify { noteRepository.getNoteByUuid(any()) }
    }

    @Test
    fun `should throw exception when note cannot be found from repository`() = runBlockingTest {
        coEvery { noteRepository.getNoteByUuid(any()) } returns null

        invoking {
            runBlockingTest {
                getNoteByUuid.execute(GetNoteByUuid.Params("uuid"))
            }
        } shouldThrow IllegalStateException::class

        coVerify { noteRepository.getNoteByUuid(any()) }
    }
}
