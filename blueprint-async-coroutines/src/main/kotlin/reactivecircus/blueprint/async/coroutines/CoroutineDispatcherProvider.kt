package reactivecircus.blueprint.async.coroutines

import kotlinx.coroutines.CoroutineDispatcher

/**
 * A wrapper class for common coroutine dispatchers.
 * An instance of this can be injected to classes which are concerned about executing code
 * on different threads, but they don't need to know about the underlying implementation.
 * A single-threaded version for example can be injected for testing.
 */
public class CoroutineDispatcherProvider(
    /**
     * Dispatcher for IO-bound work
     */
    public val io: CoroutineDispatcher,
    /**
     * Dispatcher for computational work
     */
    public val computation: CoroutineDispatcher,
    /**
     * Dispatcher for UI work
     */
    public val ui: CoroutineDispatcher
)
