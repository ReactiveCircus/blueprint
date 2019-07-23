package reactivecircus.blueprint.interactor.rx

import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.junit.Test
import reactivecircus.blueprint.interactor.EmptyParams
import reactivecircus.blueprint.interactor.InteractorParams

class CompletableInteractorTest {

    @Suppress("ForbiddenVoid")
    private lateinit var testObserver: TestObserver<Void>

    private val ioScheduler = TestScheduler()
    private val uiScheduler = TestScheduler()

    private val interactor = CompletableInteractorImpl(
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler
    )
    private val emptyParamsInteractor = EmptyParamsCompletableInteractorImpl(
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler
    )

    @Test
    fun `should complete or error based on the underlying Completable implementation`() {
        testObserver = interactor.buildCompletable(CompletableParams(shouldFail = false)).test()

        ioScheduler.triggerActions()
        uiScheduler.triggerActions()

        testObserver.await()
            .assertComplete()
            .assertNoErrors()

        testObserver = interactor.buildCompletable(CompletableParams(shouldFail = true)).test()

        ioScheduler.triggerActions()
        uiScheduler.triggerActions()

        testObserver.await()
            .assertError(IllegalArgumentException::class.java)
    }

    @Test
    fun `should complete asynchronously`() {
        testObserver = emptyParamsInteractor.buildCompletable(EmptyParams).test()

        ioScheduler.triggerActions()
        uiScheduler.triggerActions()

        testObserver.await()
            .assertComplete()
            .assertNoErrors()
    }

    private inner class CompletableInteractorImpl(
        ioScheduler: Scheduler,
        uiScheduler: Scheduler
    ) :
        CompletableInteractor<CompletableParams>(
            ioScheduler, uiScheduler
        ) {
        override fun createInteractor(params: CompletableParams): Completable {
            return if (params.shouldFail) {
                Completable.error(IllegalArgumentException())
            } else {
                Completable.complete()
            }
        }
    }

    private inner class CompletableParams(val shouldFail: Boolean) : InteractorParams

    private inner class EmptyParamsCompletableInteractorImpl(
        ioScheduler: Scheduler,
        uiScheduler: Scheduler
    ) : CompletableInteractor<EmptyParams>(
        ioScheduler, uiScheduler
    ) {
        override fun createInteractor(params: EmptyParams): Completable {
            return Completable.complete()
        }
    }
}
