package reactivecircus.blueprint.async.coroutines

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.withContext
import org.junit.Test

@ExperimentalCoroutinesApi
class CoroutineDispatcherProviderTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @Test
    fun `execute and return immediately with coroutine dispatchers backed by single thread`() =
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

            assertThat(result)
                .isEqualTo(listOf(1, 4, 9))

            val job = launch(coroutineDispatcherProvider.ui) {
                delay(1000L)
            }

            advanceTimeBy(1000L)

            assertThat(job.isCompleted)
                .isTrue()
        }

    @Test
    fun `does not not execute and return immediately with coroutine dispatchers backed by multiple threads`() =
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

            val completed = runCatching {
                deferred.getCompleted()
                true
            }.getOrElse {
                if (it is IllegalStateException) {
                    deferred.cancel()
                    false
                } else throw it
            }

            assertThat(completed)
                .isFalse()

            val job = launch(coroutineDispatcherProvider.ui) {
                delay(1000L)
            }

            advanceTimeBy(1000L)

            assertThat(job.isCompleted)
                .isFalse()

            job.cancel()
        }
}
