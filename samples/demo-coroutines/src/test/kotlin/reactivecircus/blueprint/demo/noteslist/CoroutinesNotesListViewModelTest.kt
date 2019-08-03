package reactivecircus.blueprint.demo.noteslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import reactivecircus.blueprint.demo.domain.interactor.CoroutinesStreamAllNotes
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.testutil.CoroutinesTestRule

@FlowPreview
@ExperimentalCoroutinesApi
class CoroutinesNotesListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val dummyNotes = listOf(
        Note(
            content = "Note 1",
            timeCreated = System.currentTimeMillis(),
            timeLastUpdated = System.currentTimeMillis()
        ),
        Note(
            content = "Note 2",
            timeCreated = System.currentTimeMillis(),
            timeLastUpdated = System.currentTimeMillis()
        )
    )

    private val streamAllNotes = mockk<CoroutinesStreamAllNotes>()

    private val stateObserver = mockk<Observer<State>>(relaxed = true)

    private val viewModel: CoroutinesNotesListViewModel by lazy {
        CoroutinesNotesListViewModel(streamAllNotes)
    }

    @Test
    fun `emit State#LoadingNotes when initialized`() = runBlockingTest {
        every { streamAllNotes.buildFlow(any()) } returns emptyFlow()

        viewModel.notesLiveData.observeForever(stateObserver)

        verify(exactly = 1) {
            stateObserver.onChanged(
                State.LoadingNotes
            )
        }
    }

    @Test
    fun `emit State#Idle with notes when streamAllNotes emits`() = runBlockingTest {
        val emitter = BroadcastChannel<List<Note>>(CONFLATED)
        every { streamAllNotes.buildFlow(any()) } returns emitter.asFlow()

        viewModel.notesLiveData.observeForever(stateObserver)

        verify(exactly = 1) {
            streamAllNotes.buildFlow(any())
        }

        verify(exactly = 1) {
            stateObserver.onChanged(
                State.LoadingNotes
            )
        }

        emitter.offer(dummyNotes)

        verify(exactly = 1) {
            stateObserver.onChanged(
                State.Idle(dummyNotes)
            )
        }

        val updatedDummyNotes = dummyNotes + listOf(
            Note(
                content = "Note 3",
                timeCreated = System.currentTimeMillis(),
                timeLastUpdated = System.currentTimeMillis()
            )
        )

        emitter.offer(updatedDummyNotes)

        verify(exactly = 1) {
            stateObserver.onChanged(
                State.Idle(updatedDummyNotes)
            )
        }
    }
}
