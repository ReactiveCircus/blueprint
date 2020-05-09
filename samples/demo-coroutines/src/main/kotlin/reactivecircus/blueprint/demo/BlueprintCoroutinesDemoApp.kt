package reactivecircus.blueprint.demo

import android.app.Application
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

open class BlueprintCoroutinesDemoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    @ExperimentalCoroutinesApi
    open val injector: CoroutinesAppInjector = CoroutinesAppInjector()
}
