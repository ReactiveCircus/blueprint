package reactivecircus.blueprint.testing.assertion

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.core.AllOf
import org.hamcrest.core.StringContains
import org.hamcrest.core.StringStartsWith
import reactivecircus.blueprint.testing.RobotAssertions

/**
 * Check if all texts associated with [textResIds] are displayed.
 */
fun RobotAssertions.textDisplayed(@StringRes vararg textResIds: Int) {
    textResIds.forEach { textResId ->
        Espresso.onView(ViewMatchers.withText(textResId))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}

/**
 * Check if all [texts] are displayed.
 */
fun RobotAssertions.textDisplayed(vararg texts: String) {
    texts.forEach { text ->
        Espresso.onView(ViewMatchers.withText(text))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}

/**
 * Check if no views associated with [textResIds] is displayed.
 */
fun RobotAssertions.textNotDisplayed(@StringRes vararg textResIds: Int) {
    textResIds.forEach { textResId ->
        Espresso.onView(ViewMatchers.withText(textResId))
            .check(ViewAssertions.doesNotExist())
    }
}

/**
 * Check if none of [texts] is displayed.
 */
fun RobotAssertions.textNotDisplayed(vararg texts: String) {
    texts.forEach { text ->
        Espresso.onView(ViewMatchers.withText(text))
            .check(ViewAssertions.doesNotExist())
    }
}

/**
 * Check if the view associated with [viewId] has [expected] text.
 */
fun RobotAssertions.viewHasText(@IdRes viewId: Int, expected: String) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .check(ViewAssertions.matches(ViewMatchers.withText(expected)))
}

/**
 * Check if the view associated with [viewId] has string associated with [messageResId] text.
 */
fun RobotAssertions.viewHasText(@IdRes viewId: Int, @StringRes messageResId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .check(ViewAssertions.matches(ViewMatchers.withText(messageResId)))
}

/**
 * Check if any descendant views of the view associated with [viewId] has [expected] string.
 */
fun RobotAssertions.viewContainsText(@IdRes viewId: Int, expected: String) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .check(
            ViewAssertions.matches(
                ViewMatchers.withText(
                    StringContains.containsString(
                        expected
                    )
                )
            )
        )
}

/**
 * Check if the view associated with [parentResId] has text that starts with [expected].
 */
fun RobotAssertions.viewStartsWithText(expected: String, @IdRes parentResId: Int) {
    Espresso.onView(
        AllOf.allOf<View>(
            ViewMatchers.withText(StringStartsWith.startsWith(expected)),
            ViewMatchers.isDescendantOfA(ViewMatchers.withId(parentResId))
        )
    )
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
}

/**
 * Check if the view associated with [viewId]
 * has a hint that equals to the string associated with [messageResId].
 */
fun RobotAssertions.viewHasHint(@IdRes viewId: Int, @StringRes messageResId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .check(ViewAssertions.matches(ViewMatchers.withHint(messageResId)))
}
