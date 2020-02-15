package reactivecircus.blueprint.demo.domain.interactor

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Test
import reactivecircus.blueprint.async.rx3.SchedulerProvider
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.domain.repository.RxNoteRepository

class RxStreamAllNotesTest {

    private val noteRepository = mockk<RxNoteRepository>()

    private val schedulerProvider = SchedulerProvider(
        io = Schedulers.trampoline(),
        computation = Schedulers.trampoline(),
        ui = Schedulers.trampoline()
    )

    private val streamAllNotes = RxStreamAllNotes(
        noteRepository,
        schedulerProvider
    )

    @Test
    fun `stream notes from repository`() {
        every { noteRepository.streamAllNotes() } returns Observable.just(emptyList())

        val testObserver = streamAllNotes.buildObservable(
            RxStreamAllNotes.Params(RxStreamAllNotes.SortedBy.TIME_CREATED)
        ).test()

        verify(exactly = 1) {
            noteRepository.streamAllNotes()
        }

        testObserver.assertValue(emptyList())
    }

    @Test
    fun `sort notes by TIME_CREATED descending`() {
        val note1 = Note(
            content = "note 1",
            timeCreated = 1000L,
            timeLastUpdated = 1000L
        )

        val note2 = Note(
            content = "note 2",
            timeCreated = 2000L,
            timeLastUpdated = 500L
        )

        every { noteRepository.streamAllNotes() } returns Observable.just(
            listOf(note1, note2)
        )

        val testObserver = streamAllNotes.buildObservable(
            RxStreamAllNotes.Params(RxStreamAllNotes.SortedBy.TIME_CREATED)
        ).test()

        verify(exactly = 1) {
            noteRepository.streamAllNotes()
        }

        testObserver.assertValue(listOf(note2, note1))
    }

    @Test
    fun `sort notes by TIME_LAST_UPDATED descending`() {
        val note1 = Note(
            content = "note 1",
            timeCreated = 2000L,
            timeLastUpdated = 500L
        )

        val note2 = Note(
            content = "note 2",
            timeCreated = 1000L,
            timeLastUpdated = 1000L
        )

        every { noteRepository.streamAllNotes() } returns Observable.just(
            listOf(note1, note2)
        )

        val testObserver = streamAllNotes.buildObservable(
            RxStreamAllNotes.Params(RxStreamAllNotes.SortedBy.TIME_LAST_UPDATED)
        ).test()

        verify(exactly = 1) {
            noteRepository.streamAllNotes()
        }

        testObserver.assertValue(listOf(note2, note1))
    }
}
