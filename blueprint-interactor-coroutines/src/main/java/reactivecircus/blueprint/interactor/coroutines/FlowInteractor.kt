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
    abstract val dispatcher: CoroutineDispatcher

    protected abstract fun createFlow(params: P): Flow<R>

    @ExperimentalCoroutinesApi
    fun buildFlow(params: P): Flow<R> = createFlow(params).flowOn(dispatcher)
}
