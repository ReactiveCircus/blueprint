# Blueprint Testing Robot

Inspired by the [Robot pattern][testing-robot-article], the **Blueprint Testing Robot** provides a UI testing framework for authoring structured, readable, and framework-agnostic UI tests.

Powered by Kotlin's higher-order functions, **Blueprint Testing Robot** has a comprehensive set of common view actions and assertions built-in.  
  
## Dependency

```groovy
implementation "io.github.reactivecircus.blueprint:blueprint-testing-robot:${blueprint_version}"
```

## Usage

Internally **Blueprint Testing Robot** uses [Espresso][espresso] to perform view actions and assertions on platform UI widgets as well as the [Material components][material-components], but the consumers are not aware of the Espresso framework or the Android UI toolkit / Material Components.

To write UI tests for a screen, you need:

1. A **Screen Robot** responsible for driving the screen being tested, providing DSL specific to the screen.
2. The tests themselves which use the DSL provided by the **Screen Robot**.

A test may look like this:

```kotlin
@Test
fun clickIncrementButton_counterUpdated() {
    counterScreen {
        given {
            // make sure counter is reset
            resetCounter()
        }
        perform {
            // launch Activity
            launchActivityScenario<CounterActivity>()
            
            // increment the counter
            clickIncrementButton()
        }
        check {
            // assert that counter has been updated
            counterDisplayed(expectedValue = 1)
        }
    }
}
```

The `counterScreen` block is a top-level function that you implement in your robot, which we'll look at in a minute.  

The `given`, `perform`, `check` blocks are provided by our framework to help you structure your test similar to the [Given-When-Then][given-when-then] style in **BDD**.

The `given` block is used for setting up any pre-conditions required for the test. This can be omitted if your test does not depend on any data that can survive beyond the execution of a single test.

When you are in the `perform` block, you have access to common **view actions** such as `clickView(@IdRes viewId: Int)`.

Similarly when you are in the `check` block, you have access to common **view assertions** such as `viewDisplayed(@IdRes vararg viewIds: Int)`.

However you're encouraged to write your custom actions and assertions which are specific to the screen you are testing. The `clickIncrementButton()` and `counterDisplayed(expectedValue = 1)` in the example above are custom action and assertion respectively.

To implement a Robot for the test above, create a file named `CounterRobot.kt`:

```kotlin
fun counterScreen(block: CounterRobot.() -> Unit) =
    CounterRobot().apply { block() }

class CounterRobot :
    BaseRobot<CounterRobotActions, CounterRobotAssertions>(
        CounterRobotActions(), CounterRobotAssertions()
    )

class CounterRobotActions : RobotActions() {

    fun clickSaveButton() {
        clickView(R.id.button_increment)
    }
}

class CounterRobotAssertions : RobotAssertions() {

    fun counterDisplayed(expectedValue: Int) {
        viewHasText(R.id.text_view_counter, "$expectedValue")
    }
}
```

Please check the [sample apps][samples] for more examples of writing Robots and tests.

## Building Custom RobotActions and RobotAssertions

The built-in **RobotActions** and **RobotAssertions** are defined in `BaseRobot.kt`. But if these are not enough, you can easily roll your own by implementing Kotlin extension functions on the `RobotActions` or `RobotAssertions` classes.

For example to implement a **double tap** view action, define the following extension function in your `androidTest` source:

```kotlin
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import reactivecircus.blueprint.testing.RobotActions

fun RobotActions.doubleClickView(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.doubleClick())
}
```

Now in your robot you'll have access to this view action:

```kotlin
class ImageGalleryRobotActions : RobotActions() {

    fun zoomIn() {
        doubleClickView(R.id.image_view_photo)
    }
}
```

[testing-robot-article]: https://academy.realm.io/posts/kau-jake-wharton-testing-robots/
[espresso]: https://developer.android.com/training/testing/espresso 
[material-components]: https://github.com/material-components/material-components-android
[given-when-then]: https://martinfowler.com/bliki/GivenWhenThen.html
[samples]: /samples
