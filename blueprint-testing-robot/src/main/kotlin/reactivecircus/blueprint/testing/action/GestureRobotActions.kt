package reactivecircus.blueprint.testing.action

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import reactivecircus.blueprint.testing.RobotActions

/**
 * Click on the view associated with [viewId].
 */
public fun RobotActions.clickView(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.click())
}

/**
 * Long-click on the view associated with [viewId].
 */
public fun RobotActions.longClickView(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.longClick())
}

/**
 * Swipe left on the view associated with [viewId].
 */
public fun swipeLeftOnView(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.swipeLeft())
}

/**
 * Swipe right on the view associated with [viewId].
 */
public fun swipeRightOnView(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.swipeRight())
}

/**
 * Swipe up on the view associated with [viewId].
 */
public fun swipeUpOnView(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.swipeUp())
}

/**
 * Swipe up down the view associated with [viewId].
 */
public fun swipeDownOnView(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.swipeDown())
}
