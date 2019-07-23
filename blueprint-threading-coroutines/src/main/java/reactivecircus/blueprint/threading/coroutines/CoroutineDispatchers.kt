package reactivecircus.blueprint.threading.coroutines

import kotlinx.coroutines.CoroutineDispatcher

class CoroutineDispatchers(
    val io: CoroutineDispatcher,
    val computation: CoroutineDispatcher,
    val ui: CoroutineDispatcher
)
