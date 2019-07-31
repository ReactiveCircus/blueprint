package reactivecircus.blueprint.threading.rx2

import io.reactivex.Scheduler

/**
 * A wrapper class for common Rx schedulers.
 * An instance of this can be injected to classes which are concerned about executing code
 * on different threads, but they don't need to know about the underlying implementation.
 * A single-threaded version for example can be injected for testing.
 */
class SchedulerProvider(
    /**
     * Scheduler for IO-bound work
     */
    val io: Scheduler,
    /**
     * Scheduler for computational work
     */
    val computation: Scheduler,
    /**
     * Scheduler for UI work
     */
    val ui: Scheduler
)
