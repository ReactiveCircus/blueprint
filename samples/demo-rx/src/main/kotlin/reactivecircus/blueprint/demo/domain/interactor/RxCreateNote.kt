package reactivecircus.blueprint.demo.domain.interactor

import io.reactivex.rxjava3.core.Completable
import reactivecircus.blueprint.async.rx3.SchedulerProvider
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.domain.repository.RxNoteRepository
import reactivecircus.blueprint.interactor.InteractorParams
import reactivecircus.blueprint.interactor.rx3.CompletableInteractor

class RxCreateNote(
    private val noteRepository: RxNoteRepository,
    schedulerProvider: SchedulerProvider
) : CompletableInteractor<RxCreateNote.Params>(
    ioScheduler = schedulerProvider.io,
    uiScheduler = schedulerProvider.ui
) {
    override fun createInteractor(params: Params): Completable {
        return noteRepository.addNote(params.note)
    }

    class Params(internal val note: Note) : InteractorParams
}
