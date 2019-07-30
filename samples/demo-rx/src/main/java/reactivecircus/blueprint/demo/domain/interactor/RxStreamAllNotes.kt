package reactivecircus.blueprint.demo.domain.interactor

import io.reactivex.Observable
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.domain.repository.RxNoteRepository
import reactivecircus.blueprint.interactor.InteractorParams
import reactivecircus.blueprint.interactor.rx.ObservableInteractor
import reactivecircus.blueprint.threading.rx.SchedulerProvider

class RxStreamAllNotes(
    private val noteRepository: RxNoteRepository,
    schedulerProvider: SchedulerProvider
) : ObservableInteractor<RxStreamAllNotes.Params, List<Note>>(
    ioScheduler = schedulerProvider.io,
    uiScheduler = schedulerProvider.ui
) {
    override fun createInteractor(params: Params): Observable<List<Note>> {
        return noteRepository.streamAllNotes()
            .map { notes ->
                if (params.sortedBy === SortedBy.TIME_CREATED) {
                    notes.sortedByDescending { it.timeCreated }
                } else {
                    notes.sortedByDescending { it.timeLastUpdated }
                }
            }
    }

    class Params(internal val sortedBy: SortedBy) : InteractorParams

    enum class SortedBy {
        TIME_CREATED,
        TIME_LAST_UPDATED
    }
}
