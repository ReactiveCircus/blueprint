package reactivecircus.blueprint.demo.domain.interactor

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Test
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.domain.repository.CoroutinesNoteRepository

@ExperimentalCoroutinesApi
class CoroutinesGetNoteByUuidTest {

    private val noteRepository = mockk<CoroutinesNoteRepository>()

    private val testDispatcher = StandardTestDispatcher()

    private val coroutineDispatcherProvider = mockk<CoroutineDispatcherProvider> {
        every { io } returns testDispatcher
    }

    private val getNoteByUuid = CoroutinesGetNoteByUuid(
        noteRepository = noteRepository,
        coroutineDispatcherProvider = coroutineDispatcherProvider
    )

    @Test
    fun `get note by uuid from repository`() = runTest(testDispatcher) {
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
    fun `throw exception when note cannot be found from repository`() = runTest(testDispatcher) {
        coEvery { noteRepository.getNoteByUuid(any()) } returns null

        assertThrows(IllegalStateException::class.java) {
            runTest(testDispatcher) {
                getNoteByUuid.execute(CoroutinesGetNoteByUuid.Params("uuid"))
            }
        }

        coVerify { noteRepository.getNoteByUuid(any()) }
    }
}
