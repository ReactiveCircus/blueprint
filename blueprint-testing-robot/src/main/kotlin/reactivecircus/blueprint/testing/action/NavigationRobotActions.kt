package reactivecircus.blueprint.testing.action

import android.app.Activity
import android.app.Instrumentation
import android.view.View
import android.widget.ImageButton
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.google.android.material.R
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import reactivecircus.blueprint.testing.RobotActions

/**
 * Select the bottom navigation item with [navItemTitle]
 * from the bottom navigation view associated with [bottomNavigationViewResId].
 */
public fun RobotActions.selectBottomNavigationItem(@IdRes bottomNavigationViewResId: Int, navItemTitle: String) {
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
 * Select the navigation item associated with [menuItemResId]
 * from the navigation view associated with [navigationViewResId].
 */
public fun RobotActions.selectNavigationItem(@IdRes navigationViewResId: Int, @IdRes menuItemResId: Int) {
    Espresso.onView(withId(navigationViewResId))
        .perform(NavigationViewActions.navigateTo(menuItemResId))
}

/**
 * Press the Android back button.
 */
public fun RobotActions.pressBack() {
    Espresso.pressBackUnconditionally()
}

/**
 * Click the navigation up button in the current toolbar.
 */
public fun RobotActions.clickNavigateUpButton() {
    Espresso.onView(withToolbarNavigationButton()).perform(ViewActions.click())
}

/**
 * Intercept the future intent and respond with [Activity.RESULT_OK].
 */
public fun RobotActions.interceptIntents() {
    Intents.intending(CoreMatchers.not(IntentMatchers.isInternal()))
        .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
}

/**
 * Returns a matcher that matches the navigation button in [Toolbar].
 */
private fun withToolbarNavigationButton(): Matcher<View> {
    return CoreMatchers.allOf(
        ViewMatchers.withParent(ViewMatchers.withClassName(CoreMatchers.`is`(Toolbar::class.java.name))),
        ViewMatchers.withClassName(
            CoreMatchers.anyOf(
                CoreMatchers.`is`(ImageButton::class.java.name),
                CoreMatchers.`is`(AppCompatImageButton::class.java.name)
            )
        )
    )
}
