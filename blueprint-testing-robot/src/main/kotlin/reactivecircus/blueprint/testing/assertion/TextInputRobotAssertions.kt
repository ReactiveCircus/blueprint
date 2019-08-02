package reactivecircus.blueprint.testing.assertion

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import reactivecircus.blueprint.testing.RobotAssertions
import reactivecircus.blueprint.testing.hasTextInputLayoutErrorText
import reactivecircus.blueprint.testing.noTextInputLayoutError

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
