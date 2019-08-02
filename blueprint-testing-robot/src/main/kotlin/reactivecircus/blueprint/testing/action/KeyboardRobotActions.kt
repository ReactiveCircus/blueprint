package reactivecircus.blueprint.testing.action

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import reactivecircus.blueprint.testing.RobotActions

/**
 * Close the soft keyboard.
 */
fun RobotActions.closeKeyboard(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.closeSoftKeyboard())
}

/**
 * Press the action button on the keyboard.
 */
fun RobotActions.pressKeyboardActionButton(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.pressImeActionButton())
}
