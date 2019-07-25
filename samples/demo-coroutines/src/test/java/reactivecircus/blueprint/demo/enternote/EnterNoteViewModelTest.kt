package reactivecircus.blueprint.demo.enternote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.shouldEqual
import org.junit.Rule
import org.junit.Test
import reactivecircus.blueprint.demo.domain.interactor.CreateNote
import reactivecircus.blueprint.demo.domain.interactor.GetNoteByUuid
import reactivecircus.blueprint.demo.domain.interactor.UpdateNote
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.testutil.CoroutinesTestRule

@ExperimentalCoroutinesApi
class EnterNoteViewModelTest {

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

    private val getNoteByUuid = mockk<GetNoteByUuid> {
        coEvery { execute(any()) } returns dummyNote
    }

    private val createNote = mockk<CreateNote> {
        coEvery { execute(any()) } returns Unit
    }

    private val updateNote = mockk<UpdateNote> {
        coEvery { execute(any()) } returns Unit
    }

    private val stateObserver = mockk<Observer<State>>(relaxed = true)

    private val viewModelCreateMode: EnterNoteViewModel by lazy {
        EnterNoteViewModel(
            noteUuid = null,
            getNoteByUuid = getNoteByUuid,
            createNote = createNote,
            updateNote = updateNote
        )
    }

    private val viewModelUpdateMode: EnterNoteViewModel by lazy {
        EnterNoteViewModel(
            noteUuid = noteUuid,
            getNoteByUuid = getNoteByUuid,
            createNote = createNote,
            updateNote = updateNote
        )
    }

    @Test
    fun `should emit State with null value when initialized in create mode`() = runBlockingTest {
        viewModelCreateMode.noteLiveData.observeForever(stateObserver)

        coVerify(exactly = 0) {
            getNoteByUuid.execute(any())
        }

        verify(exactly = 1) {
            stateObserver.onChanged(
                State(null)
            )
        }
    }

    @Test
    fun `should emit State with loaded Note when initialized in update mode`() = runBlockingTest {
        viewModelUpdateMode.noteLiveData.observeForever(stateObserver)

        coVerify(exactly = 1) {
            getNoteByUuid.execute(any())
        }

        verify(exactly = 1) {
            stateObserver.onChanged(
                State(dummyNote)
            )
        }
    }

    @Test
    fun `should execute CreateNote with note content`() = runBlockingTest {
        viewModelCreateMode.noteLiveData.observeForever(stateObserver)

        viewModelCreateMode.createNote(dummyNote.content)

        val slot = slot<CreateNote.Params>()

        coVerify(exactly = 1) {
            createNote.execute(param = capture(slot))
        }

        slot.captured.note.content shouldEqual dummyNote.content
    }

    @Test
    fun `should execute UpdateNote with updated note`() = runBlockingTest {
        viewModelUpdateMode.noteLiveData.observeForever(stateObserver)

        val updatedNote = dummyNote.copy(content = "updated note")
        viewModelUpdateMode.updateNote(updatedNote)

        val slot = slot<UpdateNote.Params>()

        coVerify(exactly = 1) {
            updateNote.execute(param = capture(slot))
        }

        slot.captured.note shouldEqual updatedNote
    }
}
