package reactivecircus.blueprint.interactor.rx

import io.reactivex.Completable
import io.reactivex.Scheduler
import reactivecircus.blueprint.interactor.InteractorParams

/**
 * Abstract class for a use case, representing an execution unit of asynchronous work.
 * This use case type uses [Completable] as the return type.
 * Upon subscription a use case will execute its job in the thread specified by the [ioScheduler].
 * and will post the result to the thread specified by [uiScheduler].
 */
abstract class CompletableInteractor<P : InteractorParams>(
    private val ioScheduler: Scheduler,
    private val uiScheduler: Scheduler
) {
    protected abstract fun createInteractor(params: P): Completable

    /**
     * Build a use case completable with the provided execution thread and post execution thread
     * @param params
     * @return
     */
    fun buildCompletable(params: P): Completable {
        return createInteractor(params)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
    }
}
