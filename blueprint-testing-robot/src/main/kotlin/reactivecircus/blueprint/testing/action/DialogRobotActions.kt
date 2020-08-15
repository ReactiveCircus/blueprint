package reactivecircus.blueprint.testing.action

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers

/**
 * Click on button 1 of the currently displayed dialog.
 */
public fun clickDialogButton1() {
    Espresso.onView(ViewMatchers.withId(android.R.id.button1)).perform(ViewActions.click())
}

/**
 * Click on button 2 of the currently displayed dialog.
 */
public fun clickDialogButton2() {
    Espresso.onView(ViewMatchers.withId(android.R.id.button2)).perform(ViewActions.click())
}

/**
 * Click on button 3 of the currently displayed dialog.
 */
public fun clickDialogButton3() {
    Espresso.onView(ViewMatchers.withId(android.R.id.button3)).perform(ViewActions.click())
}
