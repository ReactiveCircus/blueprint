package reactivecircus.blueprint.demo.domain.interactor

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.coInvoking
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.junit.Test
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.domain.repository.CoroutinesNoteRepository
import reactivecircus.blueprint.threading.coroutines.CoroutineDispatcherProvider

@ExperimentalCoroutinesApi
class CoroutinesGetNoteByUuidTest {

    private val noteRepository = mockk<CoroutinesNoteRepository>()

    private val coroutineDispatcherProvider = mockk<CoroutineDispatcherProvider> {
        every { io } returns TestCoroutineDispatcher()
    }

    private val getNoteByUuid = CoroutinesGetNoteByUuid(
        noteRepository = noteRepository,
        coroutineDispatcherProvider = coroutineDispatcherProvider
    )

    @Test
    fun `get note by uuid from repository`() = runBlockingTest {
        val dummyNote = Note(
            content = "note",
            timeCreated = System.currentTimeMillis(),
            timeLastUpdated = System.currentTimeMillis()
        )

        coEvery { noteRepository.getNoteByUuid(any()) } returns dummyNote

        getNoteByUuid.execute(CoroutinesGetNoteByUuid.Params("uuid")) shouldEqual dummyNote

        coVerify { noteRepository.getNoteByUuid(any()) }
    }

    @Test
    fun `throw exception when note cannot be found from repository`() = runBlockingTest {
        coEvery { noteRepository.getNoteByUuid(any()) } returns null

        coInvoking {
            getNoteByUuid.execute(CoroutinesGetNoteByUuid.Params("uuid"))
        } shouldThrow IllegalStateException::class

        coVerify { noteRepository.getNoteByUuid(any()) }
    }
}
