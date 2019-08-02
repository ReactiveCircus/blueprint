package reactivecircus.blueprint.testing.action

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers
import reactivecircus.blueprint.testing.RobotActions

/**
 * Open the drawer associated with [drawerId].
 */
fun RobotActions.openDrawer(@IdRes drawerId: Int) {
    Espresso.onView(ViewMatchers.withId(drawerId)).perform(DrawerActions.open())
}

/**
 * Close the drawer associated with [drawerId].
 */
fun RobotActions.closeDrawer(@IdRes drawerId: Int) {
    Espresso.onView(ViewMatchers.withId(drawerId)).perform(DrawerActions.close())
}
