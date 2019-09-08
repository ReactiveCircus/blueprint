package reactivecircus.blueprint.testing.action

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.google.android.material.internal.CheckableImageButton
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import reactivecircus.blueprint.testing.RobotActions
import com.google.android.material.R as MaterialR

/**
 * Enter [text] into the edit text associated with [viewId].
 */
fun RobotActions.enterTextIntoView(@IdRes viewId: Int, text: String) {
    Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.typeText(text))
}

/**
 * Replace the text in the edit text associated with [viewId] with [text].
 */
fun RobotActions.replaceTextInView(@IdRes viewId: Int, text: String) {
    Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.replaceText(text))
}

/**
 * Clear the text in the edit text associated with [viewId].
 */
fun RobotActions.clearTextInView(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.clearText())
}

/**
 * Hide the password toggle button in the [TextInputLayout] associated with [viewId].
 */
fun RobotActions.hideTextInputPasswordToggleButton(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return Matchers.allOf(
                    ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                    Matchers.instanceOf(TextInputLayout::class.java)
                )
            }

            override fun perform(uiController: UiController?, view: View?) {
                (view as? TextInputLayout)?.isEndIconVisible = false
            }

            override fun getDescription(): String {
                return "Password toggle button hidden."
            }
        })
}

/**
 * Click on either the start of end icon on the [TextInputLayout] associated with [viewId].
 */
fun RobotActions.clickTextInputLayoutIcon(@IdRes viewId: Int, endIcon: Boolean) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(TextInputLayout::class.java)
            }

            override fun perform(uiController: UiController?, view: View?) {
                val iconResId = if (endIcon) {
                    MaterialR.id.text_input_end_icon
                } else {
                    MaterialR.id.text_input_start_icon
                }
                (view as? TextInputLayout)
                    ?.findViewById<CheckableImageButton>(iconResId)
                    ?.performClick()
            }

            override fun getDescription(): String {
                return "${if (endIcon) "end" else "start"} icon clicked."
            }
        })
}

/**
 * Long click on either the start of end icon on the [TextInputLayout] associated with [viewId].
 */
fun RobotActions.longClickTextInputLayoutIcon(@IdRes viewId: Int, endIcon: Boolean) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(TextInputLayout::class.java)
            }

            override fun perform(uiController: UiController?, view: View?) {
                val iconResId = if (endIcon) {
                    MaterialR.id.text_input_end_icon
                } else {
                    MaterialR.id.text_input_start_icon
                }
                (view as? TextInputLayout)
                    ?.findViewById<CheckableImageButton>(iconResId)
                    ?.performLongClick()
            }

            override fun getDescription(): String {
                return "${if (endIcon) "end" else "start"} icon long clicked."
            }
        })
}
