package reactivecircus.blueprint.demo.domain.interactor

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.domain.repository.RxNoteRepository
import reactivecircus.blueprint.async.rx2.SchedulerProvider

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

        slot.captured shouldEqual dummyNote

        testObserver.assertComplete()
    }
}
