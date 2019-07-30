package reactivecircus.blueprint.demo.domain.interactor

import kotlinx.coroutines.CoroutineDispatcher
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.domain.repository.CoroutinesNoteRepository
import reactivecircus.blueprint.interactor.InteractorParams
import reactivecircus.blueprint.interactor.coroutines.SuspendingInteractor
import reactivecircus.blueprint.threading.coroutines.CoroutineDispatcherProvider

class CoroutinesCreateNote(
    private val noteRepository: CoroutinesNoteRepository,
    coroutineDispatcherProvider: CoroutineDispatcherProvider
) : SuspendingInteractor<CoroutinesCreateNote.Params, Unit>() {
    override val dispatcher: CoroutineDispatcher = coroutineDispatcherProvider.io

    override suspend fun doWork(params: Params) {
        noteRepository.addNote(params.note)
    }

    class Params(internal val note: Note) : InteractorParams
}
