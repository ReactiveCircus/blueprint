package reactivecircus.blueprint.testing.assertion

import androidx.annotation.StringRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import reactivecircus.blueprint.testing.RobotAssertions

/**
 * Check if a dialog with the title of a string associated with [titleResId] is displayed.
 */
public fun RobotAssertions.dialogWithTextDisplayed(@StringRes titleResId: Int) {
    Espresso.onView(ViewMatchers.withText(titleResId))
        .inRoot(RootMatchers.isDialog())
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
}

/**
 * Check if a dialog with the title [expected] is displayed.
 */
public fun RobotAssertions.dialogWithTextDisplayed(expected: String) {
    Espresso.onView(ViewMatchers.withText(expected))
        .inRoot(RootMatchers.isDialog())
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
}

/**
 * Check if a dialog with [buttonTextResId] as the button 1 label is displayed.
 */
public fun RobotAssertions.dialogWithButton1Displayed(@StringRes buttonTextResId: Int) {
    Espresso.onView(ViewMatchers.withId(android.R.id.button1))
        .inRoot(RootMatchers.isDialog())
        .check(ViewAssertions.matches(ViewMatchers.withText(buttonTextResId)))
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
}

/**
 * Check if a dialog with [buttonTextResId] as the button 2 label is displayed.
 */
public fun RobotAssertions.dialogWithButton2Displayed(@StringRes buttonTextResId: Int) {
    Espresso.onView(ViewMatchers.withId(android.R.id.button2))
        .inRoot(RootMatchers.isDialog())
        .check(ViewAssertions.matches(ViewMatchers.withText(buttonTextResId)))
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
}

/**
 * Check if a dialog with [buttonTextResId] as the button 3 label is displayed.
 */
public fun RobotAssertions.dialogWithButton3Displayed(@StringRes buttonTextResId: Int) {
    Espresso.onView(ViewMatchers.withId(android.R.id.button3))
        .inRoot(RootMatchers.isDialog())
        .check(ViewAssertions.matches(ViewMatchers.withText(buttonTextResId)))
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
}
