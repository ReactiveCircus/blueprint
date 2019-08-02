package reactivecircus.blueprint.testing.action

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import reactivecircus.blueprint.testing.RobotActions

/**
 * Enter [text] into the edit text associated with [viewId].
 */
fun RobotActions.enterTextIntoView(@IdRes viewId: Int, text: String) {
    Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.typeText(text))
}

/**
 * Replace the text in the edit text associated with [viewId] with [text].
 */
fun RobotActions.replaceTextInView(@IdRes viewId: Int, text: String) {
    Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.replaceText(text))
}

/**
 * Clear the text in the edit text associated with [viewId].
 */
fun RobotActions.clearTextInView(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.clearText())
}
