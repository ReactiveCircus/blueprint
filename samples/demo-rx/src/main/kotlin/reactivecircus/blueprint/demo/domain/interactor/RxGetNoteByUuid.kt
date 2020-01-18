package reactivecircus.blueprint.demo.domain.interactor

import io.reactivex.Maybe
import io.reactivex.Single
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.domain.repository.RxNoteRepository
import reactivecircus.blueprint.interactor.InteractorParams
import reactivecircus.blueprint.interactor.rx2.SingleInteractor
import reactivecircus.blueprint.async.rx2.SchedulerProvider

class RxGetNoteByUuid(
    private val noteRepository: RxNoteRepository,
    schedulerProvider: SchedulerProvider
) : SingleInteractor<RxGetNoteByUuid.Params, Note>(
    ioScheduler = schedulerProvider.io,
    uiScheduler = schedulerProvider.ui
) {
    override fun createInteractor(params: Params): Single<Note> {
        return noteRepository.getNoteByUuid(params.uuid)
            .switchIfEmpty(
                Maybe.error<Note>(IllegalStateException("Could not find note by uuid."))
            )
            .toSingle()
    }

    class Params(internal val uuid: String) : InteractorParams
}
