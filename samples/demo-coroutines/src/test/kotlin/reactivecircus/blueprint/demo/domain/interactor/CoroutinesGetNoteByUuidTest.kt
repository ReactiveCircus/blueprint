package reactivecircus.blueprint.demo.domain.interactor

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.domain.repository.CoroutinesNoteRepository
import reactivecircus.blueprint.testutils.assertThrows

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

        assertThat(getNoteByUuid.execute(CoroutinesGetNoteByUuid.Params("uuid")))
            .isEqualTo(dummyNote)

        coVerify { noteRepository.getNoteByUuid(any()) }
    }

    @Test
    fun `throw exception when note cannot be found from repository`() = runBlockingTest {
        coEvery { noteRepository.getNoteByUuid(any()) } returns null

        assertThrows<IllegalStateException> {
            getNoteByUuid.execute(CoroutinesGetNoteByUuid.Params("uuid"))
        }

        coVerify { noteRepository.getNoteByUuid(any()) }
    }
}
