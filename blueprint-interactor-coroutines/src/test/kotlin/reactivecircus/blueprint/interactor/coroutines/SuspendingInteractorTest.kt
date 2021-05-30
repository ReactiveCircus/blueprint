package reactivecircus.blueprint.interactor.coroutines

import com.google.common.truth.Truth.assertThat
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import reactivecircus.blueprint.interactor.EmptyParams

@ExperimentalCoroutinesApi
class SuspendingInteractorTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @Test
    fun `returns result when interactor executed successfully`() = testDispatcher.runBlockingTest {
        assertThat(CalculateSquare(testDispatcher).execute(CalculateSquare.Params(3)))
            .isEqualTo(9)
    }

    @Test(expected = IOException::class)
    fun `throws exception when interactor execution failed`() = testDispatcher.runBlockingTest {
        FailingSuspendingInteractor(testDispatcher).execute(EmptyParams)
    }
}
