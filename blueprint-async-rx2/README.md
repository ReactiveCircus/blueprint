# Blueprint Async RxJava 2

This library provides a `SchedulerProvider` class to encapsulate the threading behavior with a wrapper API.

## Dependency

```groovy
implementation "io.github.reactivecircus.blueprint:blueprint-async-rx2:${blueprint_version}"
```

## Usage
`SchedulerProvider` has 3 properties, representing the common groups of threading use cases in an app:

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
