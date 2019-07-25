package reactivecircus.blueprint.demo.domain.interactor

import kotlinx.coroutines.CoroutineDispatcher
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.domain.repository.NoteRepository
import reactivecircus.blueprint.interactor.InteractorParams
import reactivecircus.blueprint.interactor.coroutines.SuspendingInteractor
import reactivecircus.blueprint.threading.coroutines.CoroutineDispatchers

class GetNoteByUuid(
    private val noteRepository: NoteRepository,
    coroutineDispatchers: CoroutineDispatchers
) : SuspendingInteractor<GetNoteByUuid.Params, Note>() {
    override val dispatcher: CoroutineDispatcher = coroutineDispatchers.io

    override suspend fun doWork(params: Params): Note {
        return checkNotNull(noteRepository.getNoteByUuid(params.uuid)) {
            "Could not find note by uuid."
        }
    }

    class Params(internal val uuid: String) : InteractorParams
}
