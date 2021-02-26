# Change Log

## Version 1.15.0

_2021-02-26_

* Breaking change: `activity.showKeyboard()` and `Activity.hideKeyboard(focusedView: View)` have been replaced with `Window.showSoftKeyboard()` and `Window.hideSoftKeyboard()` using the `WindowInsetsControllerCompat` APIs.
* Replace deprecated `systemUiVisibility` with `WindowInsetsControllerCompat`.
* Enable IR compiler.
* Kotlin 1.4.31.
* Coroutines 1.4.2.
* Update AndroidX and MDC.

## Version 1.14.1

_2020-09-14_

* Disable IR compiler.

## Version 1.14.0

_2020-09-14_

* Breaking change: `fun Context.resolveColorAttr(@AttrRes colorAttr: Int): Int` has been removed. Use `MaterialColors` from MDC instead.
* Update `AppCompatTextView.setPrecomputedTextFuture(charSequence: charSequence, executor: Executor? = null)` to allow passing in null as `charSequence`.
* Update MDC and Kotlin.
* Enable IR compiler.
* Generate API docs with Dokka 1.4.

## Version 1.13.0

_2020-08-29_

* New `RobotAssertions.viewNotExists(@IdRes vararg viewIds: Int)` for asserting that views are not present in the view hierarchy.
* New `withItemViewType(@IdRes recyclerViewId: Int, itemViewType: Int): Matcher<View>` for matching `RecyclerView` item by `itemViewType`.

## Version 1.12.0

_2020-08-16_

* Update to Kotlin 1.4.0 and Coroutines 1.3.9.
* Enable explicit API mode.
* Update AndroidX and MDC.

## Version 1.11.0

_2020-07-23_

* Move all AndroidX dependencies to the latest stable version.

## Version 1.10.0

_2020-07-17_

* New `RobotAssertions.viewEndsWithText(@IdRes viewId: Int, expected: String)` for asserting that a view has text that ends with the expected string.
* Fix `RobotAssertions.viewStartsWithText(@IdRes viewId: Int, expected: String)`.
* Depend on `kotlin-sdtlib` instead of `kotlin-stdlib-jdk8`
* Update Coroutines, AndroidX and MDC.

## Version 1.9.0

_2020-05-30_

* Breaking change: `clickTextInputLayoutIcon(@IdRes viewId: Int, endIcon: Boolean)` has been refactored into `clickTextInputLayoutStartIcon(@IdRes viewId: Int)` and `clickTextInputLayoutEndIcon(@IdRes viewId: Int)`.
* Breaking change: `longClickTextInputLayoutIcon(@IdRes viewId: Int, endIcon: Boolean)` has been refactored into `longClickTextInputLayoutStartIcon(@IdRes viewId: Int)` and `longClickTextInputLayoutEndIcon(@IdRes viewId: Int)`.
* New `clickTextInputLayoutErrorIcon(@IdRes viewId: Int)` and `longClickTextInputLayoutErrorIcon(@IdRes viewId: Int)` extensions on the `TextInputLayout`.
* Update AGP, Gradle, Coroutines, RxJava 3, AndroidX, MDC, detekt.

## Version 1.8.1

_2020-05-10_

* Fix a publishing issue.

## Version 1.8.0

_2020-05-09_

* Breaking change: `Context.isAnimationOn` is now a property extension.
* Update Kotlin, AGP, Coroutines, RxJava 3, AndroidX, detekt, LeakCanary.
* Coroutines sample has been updated to replace `LiveData` with the new `StateFlow` introduced in Coroutines 1.3.6.

## Version 1.7.1

_2020-04-11_

* Update Kotlin, AGP, RxJava 2, RxJava 3, RxKotlin 3, AndroidX, detekt.

## Version 1.6.0

_2020-03-09_

* New `clearToolbarScrollFlags(@IdRes toolbarId: Int)` for clearing any scrolling behavior on the `toolbar`.
* Internal improvements on `blueprint-testing-robot`.

## Version 1.5.0

_2020-03-08_

* New `onRecyclerViewIdle(@IdRes recyclerViewId: Int)` for waiting until `RecyclerView` has no more pending updates.
* Fix `RecyclerViewItemCountAssertion`'s param type nullability.
* Update Kotlin, AGP, Gradle, RxJava 2, RxJava 3, RxKotlin 3, RxAndroid 3, LeakCanary, AndroidX, MDC, detekt.

## Version 1.4.0

_2020-01-19_

* Breaking change: **blueprint-threading-coroutines** has been renamed to **blueprint-async-coroutines**.
* Breaking change: **blueprint-threading-rx2** has been renamed to **blueprint-async-rx2**.
* Breaking change: **blueprint-threading-rx3** has been renamed to **blueprint-async-rx3**.
* Update Kotlin, AGP, Gradle, RxJava 2, RxJava 3, LeakCanary, AndroidX, MDC, JUnit, detekt.

## Version 1.3.1

_2019-12-03_

* Fix `RobotAssertions.textInputLayoutHasNoError` which wasn't properly checking whether error message is empty.
* Update Kotlin, AGP, Gradle, RxJava 2, LeakCanary, AndroidX, MDC, kluent, detekt.

## Version 1.3.0

_2019-10-17_

* New `RobotAction` for selecting a navigation item from the drawer.
* New `RobotAction` for clicking `TextInputLayout`'s start / end icon.
* New `RobotAction` for long clicking `TextInputLayout`'s start / end icon.
* Migrate to custom Gradle plugin for managing build configs.
* Update AGP, Gradle, RxJava 2, Coroutines, AndroidX, MDC, kluent, detekt.
* Improve samples.

## Version 1.2.0

_2019-08-28_

* **blueprint-ui** - Added new extension for setting precomputed text on `AppCompatTextView`.
* Refactor `blueprint-ui` internals.
* Disable BuildConfig generation for Android library modules.
* Stop exposing resources from transitive dependencies for Android library modules.
* Update AGP, Gradle, RxJava 2, Coroutines, detekt, LeakCanary.
* Improve documentation.

## Version 1.1.0

_2019-08-20_

* New `RobotAction` for clicking on the action button on the currently displayed snackbar.
* Refactor / simplify `blueprint-testing-robot` internals.
* Update RxJava 3 to RC2 - this is a breaking change as RxJava 3 base package now has `rxjava3`.
* Update AGP, AndroidX, Coroutines, detekt.
* Improve documentation.

## Version 1.0.1

_2019-08-02_

Fix a publishing issue. All artifacts should now be available on Maven Central.

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
