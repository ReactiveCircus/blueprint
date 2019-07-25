package reactivecircus.blueprint.demo.domain.interactor

import kotlinx.coroutines.CoroutineDispatcher
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.domain.repository.NoteRepository
import reactivecircus.blueprint.interactor.InteractorParams
import reactivecircus.blueprint.interactor.coroutines.SuspendingInteractor
import reactivecircus.blueprint.threading.coroutines.CoroutineDispatchers

class UpdateNote(
    private val noteRepository: NoteRepository,
    coroutineDispatchers: CoroutineDispatchers
) : SuspendingInteractor<UpdateNote.Params, Unit>() {
    override val dispatcher: CoroutineDispatcher = coroutineDispatchers.io

    override suspend fun doWork(params: Params) {
        noteRepository.updateNote(params.note)
    }

    class Params(internal val note: Note) : InteractorParams
}
