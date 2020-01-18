package reactivecircus.blueprint.demo.domain.interactor

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.domain.repository.RxNoteRepository
import reactivecircus.blueprint.async.rx2.SchedulerProvider

class RxGetNoteByUuidTest {

    private val noteRepository = mockk<RxNoteRepository>()

    private val schedulerProvider = SchedulerProvider(
        io = Schedulers.trampoline(),
        computation = Schedulers.trampoline(),
        ui = Schedulers.trampoline()
    )

    private val getNoteByUuid = RxGetNoteByUuid(
        noteRepository = noteRepository,
        schedulerProvider = schedulerProvider
    )

    @Test
    fun `get note by uuid from repository`() {
        val dummyNote = Note(
            content = "note",
            timeCreated = System.currentTimeMillis(),
            timeLastUpdated = System.currentTimeMillis()
        )

        every { noteRepository.getNoteByUuid(any()) } returns Maybe.just(dummyNote)

        val testObserver = getNoteByUuid.buildSingle(RxGetNoteByUuid.Params("uuid")).test()

        verify { noteRepository.getNoteByUuid(any()) }

        testObserver.assertValue(dummyNote)
    }

    @Test
    fun `throw exception when note cannot be found from repository`() {
        every { noteRepository.getNoteByUuid(any()) } returns Maybe.empty()

        val testObserver = getNoteByUuid.buildSingle(RxGetNoteByUuid.Params("uuid")).test()

        verify { noteRepository.getNoteByUuid(any()) }

        testObserver.assertError(IllegalStateException::class.java)
    }
}
