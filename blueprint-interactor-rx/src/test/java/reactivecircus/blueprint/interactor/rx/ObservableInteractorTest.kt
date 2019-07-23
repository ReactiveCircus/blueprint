package reactivecircus.blueprint.interactor.rx

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.junit.Test
import reactivecircus.blueprint.interactor.EmptyParams
import reactivecircus.blueprint.interactor.InteractorParams

class ObservableInteractorTest {

    private val dummyResult = "result"

    private lateinit var testObserver: TestObserver<String>

    private val ioScheduler = TestScheduler()
    private val uiScheduler = TestScheduler()

    private val interactor = ObservableInteractorImpl(
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler
    )
    private val emptyParamsInteractor = EmptyParamsObservableInteractorImpl(
        ioScheduler = ioScheduler,
        uiScheduler = uiScheduler
    )

    @Test
    fun `should emit value or error based on the underlying Observable implementation`() {
        testObserver = interactor.buildObservable(ObservableParams(shouldFail = false)).test()

        ioScheduler.triggerActions()
        uiScheduler.triggerActions()

        testObserver.await()
            .assertValue(dummyResult)
            .assertNoErrors()

        testObserver = interactor.buildObservable(ObservableParams(shouldFail = true)).test()

        ioScheduler.triggerActions()
        uiScheduler.triggerActions()

        testObserver.await()
            .assertNoValues()
            .assertError(IllegalArgumentException::class.java)
    }

    @Test
    fun `should emit value asynchronously`() {
        testObserver = emptyParamsInteractor.buildObservable(EmptyParams).test()

        ioScheduler.triggerActions()
        uiScheduler.triggerActions()

        testObserver.await()
            .assertValue(dummyResult)
            .assertNoErrors()
    }

    private inner class ObservableInteractorImpl(
        ioScheduler: Scheduler,
        uiScheduler: Scheduler
    ) :
        ObservableInteractor<ObservableParams, String>(
            ioScheduler, uiScheduler
        ) {
        override fun createInteractor(params: ObservableParams): Observable<String> {
            return if (params.shouldFail) {
                Observable.error(IllegalArgumentException())
            } else {
                Observable.just(dummyResult)
            }
        }
    }

    private inner class ObservableParams(val shouldFail: Boolean) : InteractorParams

    private inner class EmptyParamsObservableInteractorImpl(
        ioScheduler: Scheduler,
        uiScheduler: Scheduler
    ) : ObservableInteractor<EmptyParams, String>(
        ioScheduler, uiScheduler
    ) {
        override fun createInteractor(params: EmptyParams): Observable<String> {
            return Observable.just(dummyResult)
        }
    }
}
