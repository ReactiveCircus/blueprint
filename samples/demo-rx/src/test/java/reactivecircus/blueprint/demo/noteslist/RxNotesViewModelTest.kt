package reactivecircus.blueprint.demo.noteslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.junit.Rule
import org.junit.Test
import reactivecircus.blueprint.demo.domain.interactor.RxStreamAllNotes
import reactivecircus.blueprint.demo.domain.model.Note

class RxNotesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

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

    private val streamAllNotes = mockk<RxStreamAllNotes>()

    private val stateObserver = mockk<Observer<State>>(relaxed = true)

    private val viewModel: RxNotesViewModel by lazy {
        RxNotesViewModel(streamAllNotes)
    }

    @Test
    fun `should emit State#LoadingNotes when initialized`() {
        every { streamAllNotes.buildObservable(any()) } returns Observable.empty()

        viewModel.notesLiveData.observeForever(stateObserver)

        verify(exactly = 1) {
            stateObserver.onChanged(
                State.LoadingNotes
            )
        }
    }

    @Test
    fun `should emit State#Idle with notes when streamAllNotes emits`() {
        val emitter = PublishSubject.create<List<Note>>().toSerialized()
        every { streamAllNotes.buildObservable(any()) } returns emitter

        viewModel.notesLiveData.observeForever(stateObserver)

        verify(exactly = 1) {
            streamAllNotes.buildObservable(any())
        }

        verify(exactly = 1) {
            stateObserver.onChanged(
                State.LoadingNotes
            )
        }

        emitter.onNext(dummyNotes)

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

        emitter.onNext(updatedDummyNotes)

        verify(exactly = 1) {
            stateObserver.onChanged(
                State.Idle(updatedDummyNotes)
            )
        }
    }
}
