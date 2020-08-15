package reactivecircus.blueprint.testing.action

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import reactivecircus.blueprint.testing.RobotActions
import com.google.android.material.R as MaterialR

/**
 * Click the action button on the currently displayed Snackbar.
 */
public fun RobotActions.clickSnackbarActionButton() {
    Espresso.onView(ViewMatchers.withId(MaterialR.id.snackbar_action)).perform(ViewActions.click())
}
