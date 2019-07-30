package reactivecircus.blueprint.demo.domain.interactor

import io.reactivex.Completable
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.domain.repository.RxNoteRepository
import reactivecircus.blueprint.interactor.InteractorParams
import reactivecircus.blueprint.interactor.rx.CompletableInteractor
import reactivecircus.blueprint.threading.rx.SchedulerProvider

class RxUpdateNote(
    private val noteRepository: RxNoteRepository,
    schedulerProvider: SchedulerProvider
) : CompletableInteractor<RxUpdateNote.Params>(
    ioScheduler = schedulerProvider.io,
    uiScheduler = schedulerProvider.ui
) {
    override fun createInteractor(params: Params): Completable {
        return noteRepository.updateNote(params.note)
    }

    class Params(internal val note: Note) : InteractorParams
}
