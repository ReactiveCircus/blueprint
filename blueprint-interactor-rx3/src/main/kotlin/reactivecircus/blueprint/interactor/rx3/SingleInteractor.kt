package reactivecircus.blueprint.interactor.rx3

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import reactivecircus.blueprint.interactor.InteractorParams

/**
 * Abstract class for a use case, representing an execution unit of asynchronous work.
 * This use case type uses [Single] as the return type.
 * Upon subscription a use case will execute its job in the thread specified by the [ioScheduler].
 * and will post the result to the thread specified by [uiScheduler].
 */
public abstract class SingleInteractor<P : InteractorParams, T : Any>(
    private val ioScheduler: Scheduler,
    private val uiScheduler: Scheduler
) {

    /**
     * Create a [Single] for this interactor.
     */
    protected abstract fun createInteractor(params: P): Single<T>

    /**
     * Build a use case with the provided execution thread and post execution thread
     * @param params - parameters required for this interactor
     * @param blocking - when set to true the single will be subscribed and observed on the current thread
     */
    public fun buildSingle(params: P, blocking: Boolean = false): Single<T> {
        return if (blocking) {
            createInteractor(params)
        } else {
            createInteractor(params)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
        }
    }
}
