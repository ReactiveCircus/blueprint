package reactivecircus.blueprint.demo.domain.interactor

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import reactivecircus.blueprint.async.rx3.SchedulerProvider
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.domain.repository.RxNoteRepository
import reactivecircus.blueprint.interactor.InteractorParams
import reactivecircus.blueprint.interactor.rx3.SingleInteractor

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
                Maybe.error(IllegalStateException("Could not find note by uuid."))
            )
            .toSingle()
    }

    class Params(internal val uuid: String) : InteractorParams
}
