# Blueprint Samples

These samples showcase how you may integrate **Blueprint** in your project by implementing a simple note taking app.

There are 2 flavors of the demo app:

* [demo-coroutines][demo-coroutines] - powered by **Kotlin Coroutines and Flow** and various Blueprint libraries.
* [demo-rx][demo-rx] - powered by **RxJava 2** and various Blueprint libraries.

## Dependencies

Since the 2 demo apps have identical functionality, we are able to share a lot of common code between the apps by having a couple of common library modules.

### demo-coroutines

This [app module][demo-coroutines] uses the following Blueprint libraries:

* [blueprint-interactor-coroutines][interactor-coroutines]
* [blueprint-threading-coroutines][threading-coroutines]
* [blueprint-ui][ui]
* [blueprint-testing-robot][testing-robot]

It also depends on the [demo-common][demo-common] and [demo-testing-common][demo-testing-common] modules.

### demo-rx

This [app module][demo-rx] uses the following Blueprint libraries:

* [blueprint-interactor-rx2][interactor-rx2]
* [blueprint-threading-rx2][threading-rx2]
* [blueprint-ui][ui]
* [blueprint-testing-robot][testing-robot]

It also depends on the [demo-common][demo-common] and [demo-testing-common][demo-testing-common] modules.

### demo-common

This [library module][demo-common] includes common code shared between the 2 apps such as resources, layouts, and in-memory cache.

### demo-testing-common

This [library module][demo-testing-common] includes common UI testing infra code shared between the 2 apps such as instrumentation robots and test data.


## Comparison

The demo apps follow **[Clean Architecture][clean-architecture]**, but the only part relevant to **Blueprint** is the **domain** layer which contains **Use Cases (also known as Interactors)**.

One of the key differences between the **Coroutines-based** and **RxJava-based** implementations is how **Interactors** are implemented:

* The **Coroutines-based** implementation uses `SuspendingInteractor` for single-response tasks and `FlowInteractor` for cold streams.
* The **RxJava-based** implementation uses `SingleInteractor` and `CompletableInteractor` for single-response tasks and `ObservableInteractor` for cold streams.

|                                     | Single-response Task                        | Cold Stream            |
|-------------------------------------|---------------------------------------------|------------------------|
| **blueprint-interactor-coroutines** | `SuspendingInteractor`                      | `FlowInteractor`       |
| **blueprint-interactor-rx2**        | `SingleInteractor`, `CompletableInteractor` | `ObservableInteractor` |

Consequently the 2 different Interactor implementations expose different async primitives to downstream:

* **Coroutines-based Interactors** - expose `suspend` function and Kotlin `Flow` to consumers. 
* **RxJava-based Interactors** - expose RxJava `Single`, `Completable` and `Observable` to consumers.

Another difference is the wrapper APIs for **encapsulated threading behavior**:

* The **Coroutines-based** implementation uses `CoroutineDispatcherProvider` from the `blueprint-threading-coroutines` library.
* The **RxJava-based** implementation uses `SchedulerProvider` from the `blueprint-threading-rx2` library.

They offer the same abstraction to help with DI and testing, but one interacts with Kotlin's `CoroutineDispatcher` API and the other one interacts with RxJava's `Scheduler` API.

## Notes

While these samples implement layered-architecture and have comprehensive unit tests and UI tests, they most definitely do not represent how one should approach building a production quality app.

A number of important things are left out as our focus here is how the **Blueprint** libraries can be integrated in your codebase:

* We implemented a **very** simple Dependency Injection (Service Locator to be precise) framework. Consider using [Dagger][dagger] or [Koin][koin] in a real project.
* Different layers (e.g. **domain**, **data**, **presentation**) should probably live in their own modules in a real project.
* Both samples use AndroidX `ViewModel` and `LiveData` in the presentation layer. For better managing complex states you might want to consider using a redux-based architecture that employs **Uni-directional Data Flow**. I personally recommend [RxRedux][rxredux] and its Coroutines-based equivalent.
* In a real project you will want to set up **product flavors** for different environments (e.g. mock, staging, production).
* Obviously the apps don't talk to any backend APIs and lack persistent storage.

For a better representation of a highly-modularized, production-quality app that uses **Blueprint**, you may want to take a look at [ReleaseProbe][release-probe].

[demo-coroutines]: demo-coroutines/
[demo-rx]: demo-rx/
[demo-common]: demo-common/
[demo-testing-common]: demo-testing-common/
[interactor-coroutines]: /blueprint-interactor-coroutines/
[threading-coroutines]: /blueprint-threading-coroutines/
[interactor-rx2]: /blueprint-interactor-rx2/
[threading-rx2]: /blueprint-threading-rx2/
[ui]: /blueprint-ui/
[testing-robot]: /blueprint-testing-robot/
[clean-architecture]: http://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html
[dagger]: https://github.com/google/dagger
[koin]: https://github.com/InsertKoinIO/koin
[rxredux]: https://github.com/freeletics/RxRedux
[release-probe]: https://github.com/ReactiveCircus/release-probe
