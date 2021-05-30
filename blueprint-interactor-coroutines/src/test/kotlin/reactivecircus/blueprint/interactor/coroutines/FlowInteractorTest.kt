package reactivecircus.blueprint.interactor.coroutines

import com.google.common.truth.Truth.assertThat
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import reactivecircus.blueprint.interactor.EmptyParams

@ExperimentalCoroutinesApi
class FlowInteractorTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @Test
    fun `emits each value when flow interactor produces multiple values`() =
        testDispatcher.runBlockingTest {
            val results = FlowInteractorWithThreeEmissions(testDispatcher)
                .buildFlow(EmptyParams)
                .toList()

            assertThat(results)
                .isEqualTo(listOf(0, 1, 2))
        }

    @Test(expected = IOException::class)
    fun `catches exception thrown by flow interactor`() = testDispatcher.runBlockingTest {
        FlowInteractorWithException(testDispatcher)
            .buildFlow(EmptyParams)
            .collect()
    }
}
