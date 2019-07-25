package reactivecircus.blueprint.demo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

class ScreenTestApp : BlueprintCoroutinesDemoApp() {

    @FlowPreview
    @ExperimentalCoroutinesApi
    override val injector: Injector = ScreenTestInjector()
}
