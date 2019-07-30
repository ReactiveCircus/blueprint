package reactivecircus.blueprint.threading.rx3

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Test

class SchedulerProviderTest {

    @Test
    fun `should emit and complete immediately with single-threaded scheduler provider`() {
        val schedulerProvider = SchedulerProvider(
            io = Schedulers.trampoline(),
            computation = Schedulers.trampoline(),
            ui = Schedulers.trampoline()
        )

        val testObserver = Single.fromCallable { 3 }
            .subscribeOn(schedulerProvider.io)
            .flatMap {
                Single.fromCallable {
                    it * it
                }
                    .subscribeOn(schedulerProvider.computation)
            }
            .observeOn(schedulerProvider.ui).test()

        testObserver.assertValue(9)
            .assertComplete()
    }

    @Test
    fun `should not emit or terminate immediately with non-single-threaded scheduler provider`() {
        val schedulerProvider = SchedulerProvider(
            io = Schedulers.io(),
            computation = Schedulers.computation(),
            ui = Schedulers.newThread()
        )

        val testObserver = Single.fromCallable { 3 }
            .subscribeOn(schedulerProvider.io)
            .flatMap {
                Single.fromCallable {
                    it * it
                }
                    .subscribeOn(schedulerProvider.computation)
            }
            .observeOn(schedulerProvider.ui).test()

        testObserver.assertNoValues()
            .assertNotComplete()
            .assertNoErrors()
    }
}
