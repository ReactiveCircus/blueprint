package reactivecircus.blueprint.testing.action

import android.app.Activity
import android.app.Instrumentation
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import com.google.android.material.R
import org.hamcrest.CoreMatchers
import reactivecircus.blueprint.testing.RobotActions
import reactivecircus.blueprint.testing.withToolbarNavigationButton

/**
 * Select the bottom navigation item with [navItemTitle]
 * from the bottom navigation view associated with [bottomNavigationViewResId].
 */
fun RobotActions.selectBottomNavigationItem(@IdRes bottomNavigationViewResId: Int, navItemTitle: String) {
    Espresso.onView(
        CoreMatchers.allOf(
            ViewMatchers.withId(R.id.icon),
            ViewMatchers.hasSibling(
                ViewMatchers.hasDescendant(
                    ViewMatchers.withText(
                        navItemTitle
                    )
                )
            ),
            ViewMatchers.isDescendantOfA(ViewMatchers.withId(bottomNavigationViewResId))
        )
    )
        .perform(ViewActions.click())
}

/**
 * Press the Android back button.
 */
fun RobotActions.pressBack() {
    Espresso.pressBackUnconditionally()
}

/**
 * Click the navigation up button in the current toolbar.
 */
fun RobotActions.clickNavigateUpButton() {
    Espresso.onView(withToolbarNavigationButton()).perform(ViewActions.click())
}

/**
 * Intercept the future intent and respond with [Activity.RESULT_OK].
 */
fun RobotActions.interceptIntents() {
    Intents.intending(CoreMatchers.not(IntentMatchers.isInternal()))
        .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
}
