package reactivecircus.blueprint.demo

import kotlinx.coroutines.ExperimentalCoroutinesApi

class CoroutinesScreenTestApp : BlueprintCoroutinesDemoApp() {

    @ExperimentalCoroutinesApi
    override val injector: CoroutinesAppInjector = CoroutinesScreenTestAppInjector()
}
