package reactivecircus.blueprint.demo

import android.os.AsyncTask
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import reactivecircus.blueprint.threading.rx.SchedulerProvider

class RxScreenTestAppInjector : RxAppInjector() {

    private val testSchedulerProvider: SchedulerProvider by lazy {
        SchedulerProvider(
            io = Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR),
            computation = Schedulers.computation(),
            ui = AndroidSchedulers.mainThread()
        )
    }

    override fun provideSchedulerProvider(): SchedulerProvider {
        return testSchedulerProvider
    }
}
