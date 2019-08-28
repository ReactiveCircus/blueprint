# Change Log

## Version 1.2.0

_2019-08-28_

* **blueprint-ui** - Added new extension for setting precomputed text on `AppCompatTextView`.
* Refactor `blueprint-ui` internals.
* Disable BuildConfig generation for Android library modules.
* Stop exposing resources from transitive dependencies for Android library modules.
* Update AGP, Gradle, RxJava 2, Coroutines, detekt, LeakCanary.
* Improved documentation.

## Version 1.1.0

_2019-08-20_

* Added a new `RobotAction` for clicking on the action button on the currently displayed snackbar.
* Refactor / simplify `blueprint-testing-robot` internals.
* Update RxJava 3 to RC2 - this is a breaking change as RxJava 3 base package now has `rxjava3`.
* Update AGP, AndroidX, Coroutines, detekt.
* Improved documentation.

## Version 1.0.1

_2019-08-02_

Fixed a publishing issue. All artifacts should now be available on Maven Central.

## Version 1.0.0

_2019-08-02_

This is the initial release of Blueprint - a collection of Architectural frameworks and toolkits for bootstrapping modern Android codebases.

* New: **blueprint-interactor-coroutines** artifact - supports building Interactors based on Kotlin Coroutines and Flow.
* New: **blueprint-interactor-rx2** artifact - supports building Interactors based on RxJava 2.
* New: **blueprint-interactor-rx3** artifact - supports building Interactors based on RxJava 3. Note that it currently uses RxJava 3.0.0-RC1, and RxAndroid has not yet been updated to target RxJava 3.x yet.
* New: **blueprint-threading-coroutines** artifact - provides encapsulation of threading behavior with Kotlin `CoroutineDispatcher`.
* New: **blueprint-threading-rx2** artifact - provides encapsulation of threading behavior with RxJava 2 `Scheduler`.
* New: **blueprint-threading-rx3** artifact - provides encapsulation of threading behavior with RxJava 3 `Scheduler`. Note that it currently uses RxJava 3.0.0-RC1, and RxAndroid has not yet been updated to target RxJava 3.x yet.
* New: **blueprint-ui** artifact - Kotlin extensions and widgets for working with the Android UI toolkit.
* New: **blueprint-testing-robot** artifact - UI testing framework and DSL for authoring structured, readable, and framework-agnostic UI tests.
