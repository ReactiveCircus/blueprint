package reactivecircus.blueprint.testing.assertion

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import reactivecircus.blueprint.testing.RobotAssertions
import reactivecircus.blueprint.testing.withEmailInputType

/**
 * Check if the edit text associated with [viewId] has an email input type.
 */
fun RobotAssertions.keyboardInputTypeIsEmail(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .check(ViewAssertions.matches(withEmailInputType()))
}
