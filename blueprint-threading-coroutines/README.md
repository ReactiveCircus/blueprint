# Blueprint Threading Coroutines

This library provides a `CoroutineDispatcherProvider` class to encapsulate the threading behavior with a wrapper API.

## Dependency

```groovy
implementation "io.github.reactivecircus.blueprint:blueprint-threading-coroutines:${blueprint_version}"
```

## Usage
`CoroutineDispatcherProvider` has 3 properties, representing the common groups of threading use cases in an app:

* `io: CoroutineDispatcher` - Dispatcher for IO-bound work
* `computation: CoroutineDispatcher` - Dispatcher for computational work
* `ui: CoroutineDispatcher` - Dispatcher for UI work

An instance of this can be injected to classes which are concerned about executing code on different threads, but they don't and shouldn't need to know about the underlying implementation. A single-threaded version for example can be injected for testing.

Practically you'll likely only have 1 instance of `CoroutineDispatcherProvider` in the production environment and use DI to inject it into anywhere in the codebase where certain threading behavior is required:

```kotlin
CoroutineDispatcherProvider(
    io = Dispatchers.IO,
    computation = Dispatchers.Default,
    ui = Dispatchers.Main.immediate
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
