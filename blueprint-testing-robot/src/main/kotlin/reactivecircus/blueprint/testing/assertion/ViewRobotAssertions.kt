package reactivecircus.blueprint.testing.assertion

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers
import reactivecircus.blueprint.testing.RobotAssertions

/**
 * Check if all views associated with [viewIds] are displayed.
 */
fun RobotAssertions.viewDisplayed(@IdRes vararg viewIds: Int) {
    viewIds.forEach { viewId ->
        Espresso.onView(ViewMatchers.withId(viewId))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}

/**
 * Check if all views associated with [viewIds] are NOT displayed.
 */
fun RobotAssertions.viewNotDisplayed(@IdRes vararg viewIds: Int) {
    viewIds.forEach { viewId ->
        Espresso.onView(ViewMatchers.withId(viewId))
            .check(ViewAssertions.matches(CoreMatchers.not<View>(ViewMatchers.isDisplayed())))
    }
}

/**
 * Check if the view associated with [viewId] is enabled.
 */
fun RobotAssertions.viewEnabled(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
}

/**
 * Check if the view associated with [viewId] is disabled.
 */
fun RobotAssertions.viewDisabled(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .check(ViewAssertions.matches(CoreMatchers.not<View>(ViewMatchers.isEnabled())))
}

/**
 * Check if the view associated with [viewId] is clickable.
 */
fun RobotAssertions.viewClickable(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .check(ViewAssertions.matches(ViewMatchers.isClickable()))
}

/**
 * Check if the view associated with [viewId] is NOT clickable.
 */
fun RobotAssertions.viewNotClickable(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .check(ViewAssertions.matches(CoreMatchers.not<View>(ViewMatchers.isClickable())))
}
