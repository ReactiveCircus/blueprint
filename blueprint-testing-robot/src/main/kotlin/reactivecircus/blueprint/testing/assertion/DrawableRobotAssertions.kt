package reactivecircus.blueprint.testing.assertion

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import reactivecircus.blueprint.testing.RobotAssertions
import reactivecircus.blueprint.testing.withDrawable

/**
 * Check if the drawable associated with [resourceId] is displayed.
 */
fun RobotAssertions.drawableDisplayed(@IdRes resourceId: Int) {
    Espresso.onView(withDrawable(resourceId))
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
}
