package reactivecircus.blueprint.demo.enternote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.amshove.kluent.shouldEqual
import org.junit.Rule
import org.junit.Test
import reactivecircus.blueprint.demo.domain.interactor.RxCreateNote
import reactivecircus.blueprint.demo.domain.interactor.RxGetNoteByUuid
import reactivecircus.blueprint.demo.domain.interactor.RxUpdateNote
import reactivecircus.blueprint.demo.domain.model.Note

class RxEnterNoteViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val noteUuid = "uuid"

    private val dummyNote = Note(
        uuid = noteUuid,
        content = "Note 1",
        timeCreated = System.currentTimeMillis(),
        timeLastUpdated = System.currentTimeMillis()
    )

    private val getNoteByUuid = mockk<RxGetNoteByUuid> {
        every { buildSingle(any()) } returns Single.just(dummyNote)
    }

    private val createNote = mockk<RxCreateNote> {
        every { buildCompletable(any()) } returns Completable.complete()
    }

    private val updateNote = mockk<RxUpdateNote> {
        every { buildCompletable(any()) } returns Completable.complete()
    }

    private val stateObserver = mockk<Observer<State>>(relaxed = true)

    private val viewModelCreateMode: RxEnterNoteViewModel by lazy {
        RxEnterNoteViewModel(
            noteUuid = null,
            getNoteByUuid = getNoteByUuid,
            createNote = createNote,
            updateNote = updateNote
        )
    }

    private val viewModelUpdateMode: RxEnterNoteViewModel by lazy {
        RxEnterNoteViewModel(
            noteUuid = noteUuid,
            getNoteByUuid = getNoteByUuid,
            createNote = createNote,
            updateNote = updateNote
        )
    }

    @Test
    fun `should emit State with null value when initialized in create mode`() {
        viewModelCreateMode.noteLiveData.observeForever(stateObserver)

        verify(exactly = 0) {
            getNoteByUuid.buildSingle(any())
        }

        verify(exactly = 1) {
            stateObserver.onChanged(
                State(null)
            )
        }
    }

    @Test
    fun `should emit State with loaded Note when initialized in update mode`() {
        viewModelUpdateMode.noteLiveData.observeForever(stateObserver)

        coVerify(exactly = 1) {
            getNoteByUuid.buildSingle(any())
        }

        verify(exactly = 1) {
            stateObserver.onChanged(
                State(dummyNote)
            )
        }
    }

    @Test
    fun `should execute CreateNote with note content`() {
        viewModelCreateMode.noteLiveData.observeForever(stateObserver)

        viewModelCreateMode.createNote(dummyNote.content)

        val slot = slot<RxCreateNote.Params>()

        coVerify(exactly = 1) {
            createNote.buildCompletable(params = capture(slot))
        }

        slot.captured.note.content shouldEqual dummyNote.content
    }

    @Test
    fun `should execute UpdateNote with updated note`() {
        viewModelUpdateMode.noteLiveData.observeForever(stateObserver)

        val updatedNote = dummyNote.copy(content = "updated note")
        viewModelUpdateMode.updateNote(updatedNote)

        val slot = slot<RxUpdateNote.Params>()

        coVerify(exactly = 1) {
            updateNote.buildCompletable(params = capture(slot))
        }

        slot.captured.note shouldEqual updatedNote
    }
}
