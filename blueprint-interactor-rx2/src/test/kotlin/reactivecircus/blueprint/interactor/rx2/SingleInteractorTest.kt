package reactivecircus.blueprint.interactor.rx2

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.junit.Test
import reactivecircus.blueprint.interactor.EmptyParams
import reactivecircus.blueprint.interactor.InteractorParams

class SingleInteractorTest {

    private val dummyResult = "result"

    private lateinit var testObserver: TestObserver<String>

    private val ioScheduler = TestScheduler()
    private val uiScheduler = TestScheduler()

    private val interactor = SingleInteractorImpl(
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler
    )
    private val emptyParamsInteractor = EmptyParamsSingleInteractorImpl(
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler
    )

    @Test
    fun `emit value or fail based on the underlying Single implementation`() {
        testObserver = interactor.buildSingle(SingleParams(shouldFail = false)).test()

        ioScheduler.triggerActions()
        uiScheduler.triggerActions()

        testObserver.await()
            .assertValue(dummyResult)
            .assertNoErrors()

        testObserver = interactor.buildSingle(SingleParams(shouldFail = true)).test()

        ioScheduler.triggerActions()
        uiScheduler.triggerActions()

        testObserver.await()
            .assertNoValues()
            .assertError(IllegalArgumentException::class.java)
    }

    @Test
    fun `emit value asynchronously with non-blocking mode`() {
        testObserver = emptyParamsInteractor.buildSingle(EmptyParams).test()

        ioScheduler.triggerActions()
        uiScheduler.triggerActions()

        testObserver.await()
            .assertValue(dummyResult)
            .assertNoErrors()
    }

    @Test
    fun `emit value synchronously with blocking mode`() {
        testObserver = interactor.buildSingle(SingleParams(false), blocking = true).test()

        testObserver.await()
            .assertValue(dummyResult)
            .assertNoErrors()
    }

    private inner class SingleInteractorImpl(
        ioScheduler: Scheduler,
        uiScheduler: Scheduler
    ) :
        SingleInteractor<SingleParams, String>(
            ioScheduler,
            uiScheduler
        ) {
        override fun createInteractor(params: SingleParams): Single<String> {
            return if (params.shouldFail) {
                Single.error(IllegalArgumentException())
            } else {
                Single.just(dummyResult)
            }
        }
    }

    private inner class SingleParams(val shouldFail: Boolean) : InteractorParams

    private inner class EmptyParamsSingleInteractorImpl(
        ioScheduler: Scheduler,
        uiScheduler: Scheduler
    ) : SingleInteractor<EmptyParams, String>(
        ioScheduler,
        uiScheduler
    ) {
        override fun createInteractor(params: EmptyParams): Single<String> {
            return Single.just(dummyResult)
        }
    }
}
