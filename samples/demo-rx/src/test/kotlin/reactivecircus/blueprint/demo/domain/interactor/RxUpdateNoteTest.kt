package reactivecircus.blueprint.demo.domain.interactor

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.blueprint.async.rx3.SchedulerProvider
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.domain.repository.RxNoteRepository

class RxUpdateNoteTest {

    private val noteRepository = mockk<RxNoteRepository> {
        every { updateNote(any()) } returns Completable.complete()
    }

    private val schedulerProvider = SchedulerProvider(
        io = Schedulers.trampoline(),
        computation = Schedulers.trampoline(),
        ui = Schedulers.trampoline()
    )

    private val updateNote = RxUpdateNote(
        noteRepository = noteRepository,
        schedulerProvider = schedulerProvider
    )

    @Test
    fun `update note in repository`() {
        val dummyNote = Note(
            content = "note",
            timeCreated = System.currentTimeMillis(),
            timeLastUpdated = System.currentTimeMillis()
        )

        val testObserver = updateNote.buildCompletable(RxUpdateNote.Params(dummyNote)).test()

        val slot = slot<Note>()

        verify { noteRepository.updateNote(note = capture(slot)) }

        slot.captured shouldEqual dummyNote

        testObserver.assertComplete()
    }
}
