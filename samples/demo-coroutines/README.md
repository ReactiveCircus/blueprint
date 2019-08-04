# Blueprint Coroutines Demo

This is simple note taking app for demonstrating how to use **Blueprint** in a codebase that uses **Kotlin Coroutines and Flow**.

## Interactors

The demo app follows **[Clean Architecture][clean-architecture]**. The **Use Cases (also known as Interactors)** define and encapsulate **business rules** specific to the application.

The [blueprint-interactor-coroutines][interactor-coroutines] artifact provides 2 base classes for building Interactors:

* `SuspendingInteractor` for single-shot tasks
* `FlowInteractor` for cold streams

An example implementation of `SuspendingInteractor` in the app:

```kotlin
class CoroutinesCreateNote(
    private val noteRepository: CoroutinesNoteRepository,
    coroutineDispatcherProvider: CoroutineDispatcherProvider
) : SuspendingInteractor<CoroutinesCreateNote.Params, Unit>() {
    override val dispatcher: CoroutineDispatcher = coroutineDispatcherProvider.io

    override suspend fun doWork(params: Params) {
        noteRepository.addNote(params.note)
    }

    class Params(internal val note: Note) : InteractorParams
}
```

Call-side:

```kotlin
viewModelScope.launch {
    val time = System.currentTimeMillis()
    val newNote = Note(
        content = content,
        timeCreated = time,
        timeLastUpdated = time
    )
    createNote.execute(CoroutinesCreateNote.Params(newNote))
}
```

An example implementation of `FlowInteractor` in the app:

```kotlin
class CoroutinesStreamAllNotes(
    private val noteRepository: CoroutinesNoteRepository,
    coroutineDispatcherProvider: CoroutineDispatcherProvider
) : FlowInteractor<CoroutinesStreamAllNotes.Params, List<Note>>() {
    override val dispatcher: CoroutineDispatcher = coroutineDispatcherProvider.io

    override fun createFlow(params: Params): Flow<List<Note>> {
        return noteRepository.streamAllNotes()
            .map { notes ->
                if (params.sortedBy === SortedBy.TIME_CREATED) {
                    notes.sortedByDescending { it.timeCreated }
                } else {
                    notes.sortedByDescending { it.timeLastUpdated }
                }
            }
    }

    class Params(internal val sortedBy: SortedBy) : InteractorParams

    enum class SortedBy {
        TIME_CREATED,
        TIME_LAST_UPDATED
    }
}
```

Call-side:

```kotlin
streamAllNotes.buildFlow(CoroutinesStreamAllNotes.Params(CoroutinesStreamAllNotes.SortedBy.TIME_LAST_UPDATED))
    .map { State.Idle(it) }
    .onStart<State> {
        emit(State.LoadingNotes)
    }
    .onEach {
        // propagate value of each Flow emission to LiveData<State>
        notesLiveData.value = it
    }
    .catch {
        Timber.e(it)
    }
    // launch the collection of the Flow in the [viewModelScope] from "androidx.lifecycle:lifecycle-viewmodel-ktx"
    .launchIn(viewModelScope)
```

## Threading

The `CoroutineDispatcherProvider` from the examples above is responsible encapsulating the threading behavior with a wrapper API.

This class from the [blueprint-threading-coroutines][threading-coroutines] artifact has 3 properties, representing the common groups of threading use cases in an app:

* `io: CoroutineDispatcher` - Dispatcher for IO-bound work
* `computation: CoroutineDispatcher` - Dispatcher for computational work
* `ui: CoroutineDispatcher` - Dispatcher for UI work

An instance of this can be injected to classes which are concerned about executing code on different threads, but they don't and shouldn't need to know about the underlying implementation. A single-threaded version for example can be injected for testing.

Practically you'll likely only have 1 instance of `CoroutineDispatcherProvider` in the production environment and use DI to inject it into anywhere in the codebase where certain threading behavior is required:

```kotlin
CoroutineDispatcherProvider(
    io = Dispatchers.IO,
    computation = Dispatchers.Default,
    ui = Dispatchers.Main
)
```

In unit tests you can easily swap out the implementation to make sure code is executed in a single thread:

```kotlin
CoroutineDispatcherProvider(
    io = testCoroutineDispatcher,
    computation = testCoroutineDispatcher,
    ui = testCoroutineDispatcher
)
```

where `testCoroutineDispatcher` is an instance of `TestCoroutineDispatcher` from the `org.jetbrains.kotlinx:kotlinx-coroutines-test` library.

For a full example please look at the unit tests for Interactors e.g. `CoroutinesStreamAllNotesTest.kt`. 

## UI Widget and Extensions

The demo app uses various Kotlin extensions and widget from the [blueprint-ui][ui] artifact:

For launching a new Activity from an Activity:

```kotlin
launchActivity<CoroutinesEnterNoteActivity> {
    putExtra(EXTRA_ENTER_NOTE_PARAMS, EnterNoteParams.CreateNew)
}
```

Test-friendly ProgressBar which hides the indeterminate drawable if animation is off:

```xml
<reactivecircus.blueprint.ui.widget.IndeterminateProgressBar/>
```

## UI Testing

The UI testing framework and Testing Robot DSL from the [blueprint-testing-robot][testing-robot] provide the building blocks for authoring structured, readable, and framework-agnostic UI tests.

An example of a test case:

```kotlin
@Test
fun openNotesListScreenWithExistingNotes_notesDisplayed() {
    notesListScreen {
        given {
            noteCache.addNotes(testNotes)
        }
        perform {
            launchActivityScenario<CoroutinesNotesListActivity>()
        }
        check {
            createNoteButtonDisplayed()
            notesDisplayed(testNotes)
        }
    }
}
```

This DSL is powered by a custom **Screen Robot** implementation. Examples can be found in [demo-testing-common][demo-testing-common]. 

## Building

To compile the app:

`./gradlew demo-coroutines:assemble`

To run unit tests, lint and detekt:

`./gradlew demo-coroutines:check`

To run UI (on-device) tests:

`./gradlew demo-coroutines:connectedCheck`

[demo-testing-common]: /samples/demo-testing-common/
[interactor-coroutines]: /blueprint-interactor-coroutines/
[threading-coroutines]: /blueprint-threading-coroutines/
[ui]: /blueprint-ui/
[testing-robot]: /blueprint-testing-robot/
[clean-architecture]: http://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html
