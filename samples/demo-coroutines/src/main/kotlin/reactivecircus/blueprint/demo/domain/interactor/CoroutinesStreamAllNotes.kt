package reactivecircus.blueprint.demo.domain.interactor

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.domain.repository.CoroutinesNoteRepository
import reactivecircus.blueprint.interactor.InteractorParams
import reactivecircus.blueprint.interactor.coroutines.FlowInteractor
import reactivecircus.blueprint.threading.coroutines.CoroutineDispatcherProvider

class CoroutinesStreamAllNotes(
    private val noteRepository: CoroutinesNoteRepository,
    coroutineDispatcherProvider: CoroutineDispatcherProvider
) : FlowInteractor<CoroutinesStreamAllNotes.Params, List<Note>>() {
    override val dispatcher: CoroutineDispatcher = coroutineDispatcherProvider.io

    override fun createFlow(params: Params): Flow<List<Note>> {
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
