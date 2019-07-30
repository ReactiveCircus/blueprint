package reactivecircus.blueprint.interactor.coroutines

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.blueprint.interactor.EmptyParams
import java.io.IOException

@ExperimentalCoroutinesApi
class SuspendingInteractorTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @Test
    fun `returns result when interactor executed successfully`() = testDispatcher.runBlockingTest {
        CalculateSquare(testDispatcher).execute(CalculateSquare.Params(3)) shouldEqual 9
    }

    @Test(expected = IOException::class)
    fun `throws exception when interactor execution failed`() = testDispatcher.runBlockingTest {
        FailingSuspendingInteractor(testDispatcher).execute(EmptyParams)
    }
}
