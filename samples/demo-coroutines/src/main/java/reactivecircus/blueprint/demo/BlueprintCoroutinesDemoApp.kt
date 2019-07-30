package reactivecircus.blueprint.demo

import android.app.Application
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import timber.log.Timber

open class BlueprintCoroutinesDemoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    open val injector: CoroutinesAppInjector = CoroutinesAppInjector()
}
