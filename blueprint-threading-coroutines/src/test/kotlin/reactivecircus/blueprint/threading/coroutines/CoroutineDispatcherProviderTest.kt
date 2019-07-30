package reactivecircus.blueprint.threading.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.withContext
import org.amshove.kluent.shouldEqual
import org.junit.Test

@ExperimentalCoroutinesApi
class CoroutineDispatcherProviderTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @Test
    fun `should execute and return immediately with coroutine dispatchers backed by single thread`() =
        testDispatcher.runBlockingTest {
            val coroutineDispatcherProvider = CoroutineDispatcherProvider(
                io = testDispatcher,
                computation = testDispatcher,
                ui = testDispatcher
            )

            val result = withContext(coroutineDispatcherProvider.computation) {
                listOf(1, 2, 3).map {
                    async(coroutineDispatcherProvider.io) {
                        it * it
                    }
                }.awaitAll()
            }

            result shouldEqual listOf(1, 4, 9)

            val job = launch(coroutineDispatcherProvider.ui) {
                delay(1000L)
            }

            advanceTimeBy(1000L)

            job.isCompleted shouldEqual true
        }

    @Test
    fun `should not execute and return immediately with coroutine dispatchers backed by multiple threads`() =
        testDispatcher.runBlockingTest {
            val coroutineDispatcherProvider = CoroutineDispatcherProvider(
                io = Dispatchers.Default,
                computation = Dispatchers.Default,
                ui = Dispatchers.Default
            )

            val deferred = async(coroutineDispatcherProvider.computation) {
                delay(1000L)
                withContext(coroutineDispatcherProvider.io) {
                    delay(1000L)
                    3
                }
            }

            val completed = try {
                deferred.getCompleted()
                true
            } catch (e: IllegalStateException) {
                deferred.cancel()
                false
            }

            completed shouldEqual false

            val job = launch(coroutineDispatcherProvider.ui) {
                delay(1000L)
            }

            advanceTimeBy(1000L)

            job.isCompleted shouldEqual false

            job.cancel()
        }
}
