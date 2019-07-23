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
class CoroutineDispatchersTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @Test
    fun `should execute and return immediately with coroutine dispatchers backed by single thread`() =
        testDispatcher.runBlockingTest {
            val coroutineDispatchers = CoroutineDispatchers(
                io = testDispatcher,
                computation = testDispatcher,
                ui = testDispatcher
            )

            val result = withContext(coroutineDispatchers.computation) {
                listOf(1, 2, 3).map {
                    async(coroutineDispatchers.io) {
                        it * it
                    }
                }.awaitAll()
            }

            result shouldEqual listOf(1, 4, 9)

            val job = launch(coroutineDispatchers.ui) {
                delay(1000L)
            }

            advanceTimeBy(1000L)

            job.isCompleted shouldEqual true
        }

    @Test
    fun `should not execute and return immediately with coroutine dispatchers backed by multiple threads`() =
        testDispatcher.runBlockingTest {
            val coroutineDispatchers = CoroutineDispatchers(
                io = Dispatchers.IO,
                computation = Dispatchers.Default,
                ui = Dispatchers.Default
            )

            val deferred = async(coroutineDispatchers.computation) {
                delay(1000L)
                withContext(coroutineDispatchers.io) {
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

            val job = launch(coroutineDispatchers.ui) {
                delay(1000L)
            }

            advanceTimeBy(1000L)

            job.isCompleted shouldEqual false

            job.cancel()
        }
}
