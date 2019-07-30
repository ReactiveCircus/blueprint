package reactivecircus.blueprint.interactor.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import reactivecircus.blueprint.interactor.EmptyParams
import reactivecircus.blueprint.interactor.InteractorParams
import java.io.IOException

class CalculateSquare(
    override val dispatcher: CoroutineDispatcher
) : SuspendingInteractor<CalculateSquare.Params, Int>() {
    override suspend fun doWork(params: Params): Int {
        delay(1000L)
        return params.value * params.value
    }

    class Params(val value: Int) : InteractorParams
}

class FailingSuspendingInteractor(
    override val dispatcher: CoroutineDispatcher
) : SuspendingInteractor<EmptyParams, Unit>() {
    override suspend fun doWork(params: EmptyParams) {
        delay(1000L)
        throw IOException()
    }
}

class FlowInteractorWithThreeEmissions(
    override val dispatcher: CoroutineDispatcher
) : FlowInteractor<EmptyParams, Int>() {
    override fun createFlow(params: EmptyParams): Flow<Int> {
        return flow {
            delay(1000L)
            repeat(3) {
                emit(it)
            }
        }
    }
}

class FlowInteractorWithException(
    override val dispatcher: CoroutineDispatcher
) : FlowInteractor<EmptyParams, Int>() {
    override fun createFlow(params: EmptyParams): Flow<Int> {
        return flow {
            delay(1000L)
            throw IOException()
        }
    }
}
