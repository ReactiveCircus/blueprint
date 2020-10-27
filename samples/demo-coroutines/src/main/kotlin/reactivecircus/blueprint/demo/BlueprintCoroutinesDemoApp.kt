package reactivecircus.blueprint.demo

import android.app.Application
import timber.log.Timber

open class BlueprintCoroutinesDemoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    open val injector: CoroutinesAppInjector = CoroutinesAppInjector()
}
