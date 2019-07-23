package reactivecircus.blueprint.interactor.coroutines

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.blueprint.interactor.EmptyParams
import java.io.IOException

@ExperimentalCoroutinesApi
class FlowInteractorTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @Test
    fun `emits each value when flow interactor produces multiple values`() =
        testDispatcher.runBlockingTest {
            val results = FlowInteractorWithThreeEmissions(testDispatcher)
                .buildFlow(EmptyParams)
                .toList()

            results shouldEqual listOf(0, 1, 2)
        }

    @Test(expected = IOException::class)
    fun `catches exception thrown by flow interactor`() = testDispatcher.runBlockingTest {
        FlowInteractorWithException(testDispatcher)
            .buildFlow(EmptyParams)
            .collect()
    }
}
