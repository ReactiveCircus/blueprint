package reactivecircus.blueprint.testing.assertion

import android.text.InputType
import android.view.View
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import reactivecircus.blueprint.testing.RobotAssertions

/**
 * Check if the edit text associated with [viewId] has an email input type.
 */
fun RobotAssertions.keyboardInputTypeIsEmail(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .check(ViewAssertions.matches(withEmailInputType()))
}

/**
 * Returns a matcher that matches the [EditText] with the Email input type.
 */
private fun withEmailInputType(): Matcher<View> {
    return object : BoundedMatcher<View, EditText>(EditText::class.java) {

        override fun matchesSafely(item: EditText): Boolean {
            return item.inputType and InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS > 0
        }

        override fun describeTo(description: Description) {
            description.appendText("with error: The input type of the EditText is not Email Address input type.")
        }
    }
}
