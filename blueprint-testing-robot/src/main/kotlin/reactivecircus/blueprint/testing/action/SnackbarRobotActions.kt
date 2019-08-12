package reactivecircus.blueprint.testing.action

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import reactivecircus.blueprint.testing.R
import reactivecircus.blueprint.testing.RobotActions

/**
 * Click the action button on the currently displayed Snackbar.
 */
fun RobotActions.clickSnackbarActionButton() {
    Espresso.onView(ViewMatchers.withId(R.id.snackbar_action)).perform(ViewActions.click())
}
