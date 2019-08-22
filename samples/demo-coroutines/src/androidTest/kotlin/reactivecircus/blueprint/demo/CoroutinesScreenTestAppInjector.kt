package reactivecircus.blueprint.demo

import android.os.AsyncTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.asCoroutineDispatcher
import reactivecircus.blueprint.threading.coroutines.CoroutineDispatcherProvider

@FlowPreview
@ExperimentalCoroutinesApi
class CoroutinesScreenTestAppInjector : CoroutinesAppInjector() {

    private val testCoroutineDispatcherProvider: CoroutineDispatcherProvider by lazy {
        CoroutineDispatcherProvider(
            io = AsyncTask.THREAD_POOL_EXECUTOR.asCoroutineDispatcher(),
            computation = Dispatchers.Default,
            ui = Dispatchers.Main.immediate
        )
    }

    override fun provideCoroutineDispatcherProvider(): CoroutineDispatcherProvider {
        return testCoroutineDispatcherProvider
    }
}
