package reactivecircus.blueprint.testutils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Assert
import org.junit.function.ThrowingRunnable

@ExperimentalCoroutinesApi
inline fun <reified T : Throwable> TestCoroutineScope.assertThrows(
    crossinline runnable: suspend () -> Unit
): T {
    val throwingRunnable = ThrowingRunnable {
        val job = async { runnable() }
        job.getCompletionExceptionOrNull()?.run { throw this }
        job.cancel()
    }
    return Assert.assertThrows(T::class.java, throwingRunnable)
}
