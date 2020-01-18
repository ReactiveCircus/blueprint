package reactivecircus.blueprint.demo.domain.interactor

import kotlinx.coroutines.CoroutineDispatcher
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.domain.repository.CoroutinesNoteRepository
import reactivecircus.blueprint.interactor.InteractorParams
import reactivecircus.blueprint.interactor.coroutines.SuspendingInteractor
import reactivecircus.blueprint.async.coroutines.CoroutineDispatcherProvider

class CoroutinesGetNoteByUuid(
    private val noteRepository: CoroutinesNoteRepository,
    coroutineDispatcherProvider: CoroutineDispatcherProvider
) : SuspendingInteractor<CoroutinesGetNoteByUuid.Params, Note>() {
    override val dispatcher: CoroutineDispatcher = coroutineDispatcherProvider.io

    override suspend fun doWork(params: Params): Note {
        return checkNotNull(noteRepository.getNoteByUuid(params.uuid)) {
            "Could not find note by uuid."
        }
    }

    class Params(internal val uuid: String) : InteractorParams
}
