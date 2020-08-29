package reactivecircus.blueprint.testing.assertion

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import org.hamcrest.CoreMatchers.not
import reactivecircus.blueprint.testing.RobotAssertions

/**
 * Check if all views associated with [viewIds] are displayed.
 */
public fun RobotAssertions.viewDisplayed(@IdRes vararg viewIds: Int) {
    viewIds.forEach { viewId ->
        onView(ViewMatchers.withId(viewId))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}

/**
 * Check if all views associated with [viewIds] are NOT displayed.
 */
public fun RobotAssertions.viewNotDisplayed(@IdRes vararg viewIds: Int) {
    viewIds.forEach { viewId ->
        onView(ViewMatchers.withId(viewId))
            .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
    }
}

/**
 * Check if all views associated with [viewIds] are not present in the view hierarchy.
 */
public fun RobotAssertions.viewNotExists(@IdRes vararg viewIds: Int) {
    viewIds.forEach { viewId ->
        onView(ViewMatchers.withId(viewId)).check(doesNotExist())
    }
}

/**
 * Check if the view associated with [viewId] is enabled.
 */
public fun RobotAssertions.viewEnabled(@IdRes viewId: Int) {
    onView(ViewMatchers.withId(viewId))
        .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
}

/**
 * Check if the view associated with [viewId] is disabled.
 */
public fun RobotAssertions.viewDisabled(@IdRes viewId: Int) {
    onView(ViewMatchers.withId(viewId))
        .check(ViewAssertions.matches(not(ViewMatchers.isEnabled())))
}

/**
 * Check if the view associated with [viewId] is clickable.
 */
public fun RobotAssertions.viewClickable(@IdRes viewId: Int) {
    onView(ViewMatchers.withId(viewId))
        .check(ViewAssertions.matches(isClickable()))
}

/**
 * Check if the view associated with [viewId] is NOT clickable.
 */
public fun RobotAssertions.viewNotClickable(@IdRes viewId: Int) {
    onView(ViewMatchers.withId(viewId))
        .check(ViewAssertions.matches(not(isClickable())))
}
