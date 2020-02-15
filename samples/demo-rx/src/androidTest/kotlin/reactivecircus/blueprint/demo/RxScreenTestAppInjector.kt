package reactivecircus.blueprint.demo

import android.os.AsyncTask
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import reactivecircus.blueprint.async.rx3.SchedulerProvider

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
