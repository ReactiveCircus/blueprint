# Blueprint RxJava Demo

This is simple note taking app for demonstrating how to use **Blueprint** in a codebase that uses **RxJava**.

## Interactors

The demo app follows **[Clean Architecture][clean-architecture]**. The **Use Cases (also known as Interactors)** define and encapsulate **business rules** specific to the application.

The [blueprint-interactor-rx3][interactor-rx3] artifact provides 3 base classes for building Interactors:

* `SingleInteractor` for single-shot (with result) tasks
* `CompletableInteractor` for single-shot (no result) tasks
* `ObservableInteractor` for cold streams

An example implementation of `SingleInteractor` in the app:

```kotlin
class RxGetNoteByUuid(
    private val noteRepository: RxNoteRepository,
    schedulerProvider: SchedulerProvider
) : SingleInteractor<RxGetNoteByUuid.Params, Note>(
    ioScheduler = schedulerProvider.io,
    uiScheduler = schedulerProvider.ui
) {
    override fun createInteractor(params: Params): Single<Note> {
        return noteRepository.getNoteByUuid(params.uuid)
            .switchIfEmpty(
                Maybe.error(IllegalStateException("Could not find note by uuid."))
            )
            .toSingle()
    }

    class Params(internal val uuid: String) : InteractorParams
}
```

Call-side:

```kotlin
disposable += getNoteByUuid.buildSingle(RxGetNoteByUuid.Params(noteUuid))
    .subscribeBy(
        onSuccess = { note ->
            noteLiveData.value = State(note)
        },
        onError = {
            Timber.e(it)
        }
    )
```

An example implementation of `CompletableInteractor` in the app:

```kotlin
class RxUpdateNote(
    private val noteRepository: RxNoteRepository,
    schedulerProvider: SchedulerProvider
) : CompletableInteractor<RxUpdateNote.Params>(
    ioScheduler = schedulerProvider.io,
    uiScheduler = schedulerProvider.ui
) {
    override fun createInteractor(params: Params): Completable {
        return noteRepository.updateNote(params.note)
    }

    class Params(internal val note: Note) : InteractorParams
}
```

Call-side:

```kotlin
disposable += updateNote.buildCompletable(RxUpdateNote.Params(updatedNote)).subscribeBy(
        onComplete = {
            Timber.d("Note updated.")
        },
        onError = {
            Timber.e(it)
        }
    )
```

An example implementation of `ObservableInteractor` in the app:

```kotlin
class RxStreamAllNotes(
    private val noteRepository: RxNoteRepository,
    schedulerProvider: SchedulerProvider
) : ObservableInteractor<RxStreamAllNotes.Params, List<Note>>(
    ioScheduler = schedulerProvider.io,
    uiScheduler = schedulerProvider.ui
) {
    override fun createInteractor(params: Params): Observable<List<Note>> {
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
disposable += streamAllNotes
    .buildObservable(
        RxStreamAllNotes.Params(RxStreamAllNotes.SortedBy.TIME_LAST_UPDATED)
    )
    .map<State> { State.Idle(it) }
    .startWith(State.LoadingNotes)
    .subscribeBy(
        onNext = {
            notesLiveData.value = it
        },
        onError = {
            Timber.e(it)
        }
    )
```

## Threading

The `SchedulerProvider` from the examples above is responsible encapsulating the threading behavior with a wrapper API.

This class from the [blueprint-async-rx3][async-rx3] artifact has 3 properties, representing the common groups of threading use cases in an app:

* `io: Scheduler` - Scheduler for IO-bound work
* `computation: Scheduler` - Scheduler for computational work
* `ui: Scheduler` - Scheduler for UI work

An instance of this can be injected to classes which are concerned about executing code on different threads, but they don't and shouldn't need to know about the underlying implementation. A single-threaded version for example can be injected for testing.

Practically you'll likely only have 1 instance of `SchedulerProvider` in the production environment and use DI to inject it into anywhere in the codebase where certain threading behavior is required:

```kotlin
SchedulerProvider(
    io = Schedulers.io(),
    computation = Schedulers.computation(),
    ui = AndroidSchedulers.mainThread()
)
```

In unit tests you can easily swap out the implementation to make sure code is executed in a single thread:

```kotlin
SchedulerProvider(
    io = Schedulers.trampoline(),
    computation = Schedulers.trampoline(),
    ui = Schedulers.trampoline()
)
```

For a full example please look at the unit tests for Interactors e.g. [RxStreamAllNotesTest.kt][sample-interactor-test]. 

## UI Widget and Extensions

The demo app uses various Kotlin extensions and widget from the [blueprint-ui][ui] artifact:

For launching a new Activity from an Activity:

```kotlin
launchActivity<RxEnterNoteActivity> {
    putExtra(EXTRA_ENTER_NOTE_PARAMS, EnterNoteParams.CreateNew)
}
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
            launchActivityScenario<RxNotesListActivity>()
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

`./gradlew demo-rx:assemble`

To run unit tests, lint and detekt:

`./gradlew demo-rx:check`

To run UI (on-device) tests:

`./gradlew demo-rx:connectedCheck`


[demo-testing-common]: ../demo-testing-common/
[interactor-rx3]: ../../blueprint-interactor-rx3/
[async-rx3]: ../../blueprint-async-rx3/
[sample-interactor-test]: https://github.com/ReactiveCircus/blueprint/tree/main/samples/demo-rx/src/test/kotlin/reactivecircus/blueprint/demo/domain/interactor/RxStreamAllNotesTest.kt
[ui]: ../../blueprint-ui/
[testing-robot]: ../../blueprint-testing-robot/
[clean-architecture]: http://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html
