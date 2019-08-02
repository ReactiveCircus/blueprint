package reactivecircus.blueprint.testing.assertion

import android.app.Activity
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import org.junit.Assert
import reactivecircus.blueprint.testing.ActivityNotDisplayedAssertion
import reactivecircus.blueprint.testing.RobotAssertions
import reactivecircus.blueprint.testing.currentActivity
import reactivecircus.blueprint.testing.hasSelectedNavigationItem

/**
 * Check if the bottom navigation view associated with [bottomNavigationViewResId]
 * has [selectedItemResId] as the selected item.
 */
fun RobotAssertions.bottomNavigationViewItemSelected(
    @IdRes bottomNavigationViewResId: Int,
    @IdRes selectedItemResId: Int
) {
    Espresso.onView(ViewMatchers.withId(bottomNavigationViewResId))
        .check(ViewAssertions.matches(hasSelectedNavigationItem(selectedItemResId)))
}

/**
 * Check if no activity is currently displayed
 */
fun RobotAssertions.noActivityDisplayed() {
    ActivityNotDisplayedAssertion(currentActivity())
}

/**
 * Check if the activity of type [A] has been launched.
 */
inline fun <reified A : Activity> RobotAssertions.activityLaunched() {
    Intents.intended(IntentMatchers.hasComponent(A::class.java.name))
}

/**
 * Check if the fragment of type [F] with [tag] is displayed.
 */
inline fun <reified F : Fragment> RobotAssertions.fragmentDisplayed(tag: String) {
    val fragment =
        (currentActivity() as FragmentActivity).supportFragmentManager.findFragmentByTag(tag)
    Assert.assertTrue(fragment != null && fragment.isVisible && fragment is F)
}

/**
 * Check if the fragment of type [F]
 * with a navigation host associated with [navHostViewId] is displayed.
 */
inline fun <reified F : Fragment> RobotAssertions.fragmentDisplayed(@IdRes navHostViewId: Int) {
    val fragment = (currentActivity() as? FragmentActivity)?.supportFragmentManager
        ?.findFragmentById(navHostViewId)?.childFragmentManager?.primaryNavigationFragment

    Assert.assertTrue(fragment != null && fragment.isVisible && fragment is F)
}
