package reactivecircus.blueprint.testing.assertion

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import reactivecircus.blueprint.testing.RobotAssertions

/**
 * Check if text input layout associated with [viewId] has [errorMessage] as the error.
 */
fun RobotAssertions.textInputLayoutHasError(@IdRes viewId: Int, errorMessage: String) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .check(ViewAssertions.matches(hasTextInputLayoutErrorText(errorMessage)))
}

/**
 * Check if text input layout associated with [viewId]
 * has string associated with [errorMessageResId] as the error.
 */
fun RobotAssertions.textInputLayoutHasError(@IdRes viewId: Int, @StringRes errorMessageResId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .check(ViewAssertions.matches(hasTextInputLayoutErrorText(errorMessageResId)))
}

/**
 * Check if the text input layout associated with [viewId] has NO error.
 */
fun RobotAssertions.textInputLayoutHasNoError(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .check(ViewAssertions.matches(noTextInputLayoutError()))
}

/**
 * Returns a matcher that matches a [TextInputLayout] with no error.
 */
private fun noTextInputLayoutError(): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
        override fun matchesSafely(view: View): Boolean {
            return view is TextInputLayout && !view.isErrorEnabled
        }

        override fun describeTo(description: Description) {
            description.appendText("no error.")
        }
    }
}

/**
 * Returns a matcher that matches a [TextInputLayout] with a string associated with [errorMessageResId] as error.
 */
private fun hasTextInputLayoutErrorText(@StringRes errorMessageResId: Int): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
        override fun matchesSafely(view: View): Boolean {
            if (view !is TextInputLayout || view.error == null) {
                return false
            }

            val hint = view.error.toString()

            return view.getResources().getString(errorMessageResId) == hint
        }

        override fun describeTo(description: Description) {
            description.appendText("with error string resource id: $errorMessageResId")
        }
    }
}

/**
 * Returns a matcher that matches a [TextInputLayout] with the [expectedErrorText] as error.
 */
private fun hasTextInputLayoutErrorText(expectedErrorText: String): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
        override fun matchesSafely(view: View): Boolean {
            if (view !is TextInputLayout || view.error == null) {
                return false
            }

            val hint = view.error.toString()

            return expectedErrorText == hint
        }

        override fun describeTo(description: Description) {
            description.appendText("with error: $expectedErrorText")
        }
    }
}
