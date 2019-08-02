package reactivecircus.blueprint.threading.rx3

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Test

class SchedulerProviderTest {

    @Test
    fun `emit and complete immediately with blocking scheduler provider`() {
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
    fun `does not emit or terminate immediately with non-blocking scheduler provider`() {
        val schedulerProvider = SchedulerProvider(
            io = Schedulers.single(),
            computation = Schedulers.single(),
            ui = Schedulers.single()
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
