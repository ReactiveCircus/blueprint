package reactivecircus.blueprint.interactor.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import reactivecircus.blueprint.interactor.InteractorParams

/**
 * An interactor (use case in Clean Architecture) represents an execution unit of asynchronous work.
 * A [FlowInteractor] exposes a cold stream of values implemented with Kotlin [Flow].
 *
 * Work will be executed on thread as specified by the [dispatcher] of the interactor.
 */
abstract class FlowInteractor<in P : InteractorParams, out R> {

    /**
     * The coroutine context this interactor should execute on.
     */
    abstract val dispatcher: CoroutineDispatcher

    /**
     * Create a [Flow] for this interactor.
     */
    protected abstract fun createFlow(params: P): Flow<R>

    /**
     * Build a new [Flow] from this interactor.
     */
    @ExperimentalCoroutinesApi
    fun buildFlow(params: P): Flow<R> = createFlow(params).flowOn(dispatcher)
}
