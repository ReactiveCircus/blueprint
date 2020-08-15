package reactivecircus.blueprint.interactor.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import reactivecircus.blueprint.interactor.InteractorParams

/**
 * An interactor (use case in Clean Architecture) represents an execution unit of asynchronous work.
 * A [SuspendingInteractor] returns a single response through a suspend function.
 *
 * Work will be executed on thread as specified by the [dispatcher] of the interactor.
 */
public abstract class SuspendingInteractor<in P : InteractorParams, out R> {

    /**
     * The coroutine context this interactor should execute on.
     */
    public abstract val dispatcher: CoroutineDispatcher

    /**
     * Define the work to be performed by this interactor.
     */
    protected abstract suspend fun doWork(params: P): R

    /**
     * Execute the the interactor.
     */
    public suspend fun execute(params: P): R = withContext(context = dispatcher) {
        doWork(params)
    }
}
