package reactivecircus.blueprint.demo

class CoroutinesScreenTestApp : BlueprintCoroutinesDemoApp() {
    override val injector: CoroutinesAppInjector = CoroutinesScreenTestAppInjector()
}
