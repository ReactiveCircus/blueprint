package reactivecircus.blueprint.demo

import android.os.AsyncTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.asCoroutineDispatcher
import reactivecircus.blueprint.threading.coroutines.CoroutineDispatchers

@FlowPreview
@ExperimentalCoroutinesApi
class CoroutinesScreenTestAppInjector : CoroutinesAppInjector() {

    private val testCoroutineDispatchers: CoroutineDispatchers by lazy {
        CoroutineDispatchers(
            io = AsyncTask.THREAD_POOL_EXECUTOR.asCoroutineDispatcher(),
            computation = Dispatchers.Default,
            ui = Dispatchers.Main
        )
    }

    override fun provideCoroutineDispatchers(): CoroutineDispatchers {
        return testCoroutineDispatchers
    }
}
