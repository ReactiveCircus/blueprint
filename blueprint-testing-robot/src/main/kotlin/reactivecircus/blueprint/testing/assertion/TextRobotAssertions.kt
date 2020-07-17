package reactivecircus.blueprint.testing.assertion

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.core.StringContains
import org.hamcrest.core.StringEndsWith
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
 * Check if the view associated with [viewId] has text that contains the [expected] string.
 */
fun RobotAssertions.viewContainsText(@IdRes viewId: Int, expected: String) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .check(
            ViewAssertions.matches(ViewMatchers.withText(StringContains.containsString(expected)))
        )
}

/**
 * Check if the view associated with [viewId] has text that starts with the [expected] string.
 */
fun RobotAssertions.viewStartsWithText(@IdRes viewId: Int, expected: String) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .check(
            ViewAssertions.matches(ViewMatchers.withText(StringStartsWith.startsWith(expected)))
        )
}

/**
 * Check if the view associated with [viewId] has text that ends with the [expected] string.
 */
fun RobotAssertions.viewEndsWithText(@IdRes viewId: Int, expected: String) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .check(
            ViewAssertions.matches(ViewMatchers.withText(StringEndsWith.endsWith(expected)))
        )
}

/**
 * Check if the view associated with [viewId]
 * has a hint that equals to the string associated with [messageResId].
 */
fun RobotAssertions.viewHasHint(@IdRes viewId: Int, @StringRes messageResId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .check(ViewAssertions.matches(ViewMatchers.withHint(messageResId)))
}
