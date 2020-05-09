package reactivecircus.blueprint.demo.enternote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.shouldEqual
import org.junit.Rule
import org.junit.Test
import reactivecircus.blueprint.demo.domain.interactor.CoroutinesCreateNote
import reactivecircus.blueprint.demo.domain.interactor.CoroutinesGetNoteByUuid
import reactivecircus.blueprint.demo.domain.interactor.CoroutinesUpdateNote
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.testutil.CoroutinesTestRule

@ExperimentalCoroutinesApi
class CoroutinesEnterNoteViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val noteUuid = "uuid"

    private val dummyNote = Note(
        uuid = noteUuid,
        content = "Note 1",
        timeCreated = System.currentTimeMillis(),
        timeLastUpdated = System.currentTimeMillis()
    )

    private val getNoteByUuid = mockk<CoroutinesGetNoteByUuid> {
        coEvery { execute(any()) } returns dummyNote
    }

    private val createNote = mockk<CoroutinesCreateNote> {
        coEvery { execute(any()) } returns Unit
    }

    private val updateNote = mockk<CoroutinesUpdateNote> {
        coEvery { execute(any()) } returns Unit
    }

    private val viewModelCreateMode: CoroutinesEnterNoteViewModel by lazy {
        CoroutinesEnterNoteViewModel(
            noteUuid = null,
            getNoteByUuid = getNoteByUuid,
            createNote = createNote,
            updateNote = updateNote
        )
    }

    private val viewModelUpdateMode: CoroutinesEnterNoteViewModel by lazy {
        CoroutinesEnterNoteViewModel(
            noteUuid = noteUuid,
            getNoteByUuid = getNoteByUuid,
            createNote = createNote,
            updateNote = updateNote
        )
    }

    @Test
    fun `emit State with null value when initialized in create mode`() = runBlockingTest {
        viewModelCreateMode.noteStateFlow.take(1).single() shouldEqual State.Idle(null)

        coVerify(exactly = 0) {
            getNoteByUuid.execute(any())
        }
    }

    @Test
    fun `emit State with loaded Note when initialized in update mode`() = runBlockingTest {
        viewModelUpdateMode.noteStateFlow.take(1).single() shouldEqual State.Idle(dummyNote)

        coVerify(exactly = 1) {
            getNoteByUuid.execute(any())
        }
    }

    @Test
    fun `execute CreateNote with new note content`() = runBlockingTest {
        viewModelCreateMode.createNote(dummyNote.content)

        val slot = slot<CoroutinesCreateNote.Params>()

        coVerify(exactly = 1) {
            createNote.execute(params = capture(slot))
        }

        slot.captured.note.content shouldEqual dummyNote.content
    }

    @Test
    fun `execute UpdateNote with updated note`() = runBlockingTest {
        val updatedNote = dummyNote.copy(content = "updated note")
        viewModelUpdateMode.updateNote(updatedNote)

        val slot = slot<CoroutinesUpdateNote.Params>()

        coVerify(exactly = 1) {
            updateNote.execute(params = capture(slot))
        }

        slot.captured.note shouldEqual updatedNote
    }
}
