package reactivecircus.blueprint.demo.domain.interactor

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Test
import reactivecircus.blueprint.async.rx3.SchedulerProvider
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.domain.repository.RxNoteRepository

class RxCreateNoteTest {

    private val noteRepository = mockk<RxNoteRepository> {
        every { addNote(any()) } returns Completable.complete()
    }

    private val schedulerProvider = SchedulerProvider(
        io = Schedulers.trampoline(),
        computation = Schedulers.trampoline(),
        ui = Schedulers.trampoline()
    )

    private val createNote = RxCreateNote(
        noteRepository = noteRepository,
        schedulerProvider = schedulerProvider
    )

    @Test
    fun `add note through repository`() {
        val dummyNote = Note(
            content = "note",
            timeCreated = System.currentTimeMillis(),
            timeLastUpdated = System.currentTimeMillis()
        )

        val testObserver = createNote.buildCompletable(RxCreateNote.Params(dummyNote)).test()

        val slot = slot<Note>()

        verify { noteRepository.addNote(note = capture(slot)) }

        assertThat(slot.captured)
            .isEqualTo(dummyNote)

        testObserver.assertComplete()
    }
}
