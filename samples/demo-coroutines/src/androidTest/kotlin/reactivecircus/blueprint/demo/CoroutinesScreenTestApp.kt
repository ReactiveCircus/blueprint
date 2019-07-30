package reactivecircus.blueprint.demo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

class CoroutinesScreenTestApp : BlueprintCoroutinesDemoApp() {

    @FlowPreview
    @ExperimentalCoroutinesApi
    override val injector: CoroutinesAppInjector = CoroutinesScreenTestAppInjector()
}
