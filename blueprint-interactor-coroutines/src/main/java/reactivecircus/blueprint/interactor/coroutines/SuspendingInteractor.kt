package reactivecircus.blueprint.interactor.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import reactivecircus.blueprint.interactor.InteractorParams

/**
 * An interactor (use case in Clean Architecture) represents an execution unit of asynchronous work.
 * A [SuspendingInteractor] returns a single value response through a suspend function.
 *
 * Work will be executed on thread as specified by the [dispatcher] of the interactor.
 */
abstract class SuspendingInteractor<in P : InteractorParams, out R> {
    abstract val dispatcher: CoroutineDispatcher

    protected abstract suspend fun doWork(params: P): R

    suspend fun execute(param: P): R = withContext(context = dispatcher) {
        doWork(param)
    }
}
