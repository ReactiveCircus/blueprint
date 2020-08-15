package reactivecircus.blueprint.testing.assertion

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.DrawerMatchers
import androidx.test.espresso.matcher.ViewMatchers
import reactivecircus.blueprint.testing.RobotAssertions

/**
 * Check if the drawer associated with [drawerId] is opened.
 */
public fun RobotAssertions.drawerOpened(@IdRes drawerId: Int) {
    Espresso.onView(ViewMatchers.withId(drawerId))
        .check(ViewAssertions.matches(DrawerMatchers.isOpen()))
}

/**
 * Check if the drawer associated with [drawerId] is closed.
 */
public fun RobotAssertions.drawerClosed(@IdRes drawerId: Int) {
    Espresso.onView(ViewMatchers.withId(drawerId))
        .check(ViewAssertions.matches(DrawerMatchers.isClosed()))
}
