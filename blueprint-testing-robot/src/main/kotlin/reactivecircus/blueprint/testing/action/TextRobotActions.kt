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
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import reactivecircus.blueprint.testing.RobotActions
import com.google.android.material.R as MaterialR

/**
 * Enter [text] into the edit text associated with [viewId].
 */
public fun RobotActions.enterTextIntoView(@IdRes viewId: Int, text: String) {
    Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.typeText(text))
}

/**
 * Replace the text in the edit text associated with [viewId] with [text].
 */
public fun RobotActions.replaceTextInView(@IdRes viewId: Int, text: String) {
    Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.replaceText(text))
}

/**
 * Clear the text in the edit text associated with [viewId].
 */
public fun RobotActions.clearTextInView(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.clearText())
}

/**
 * Hide the password toggle button in the [TextInputLayout] associated with [viewId].
 */
public fun RobotActions.hideTextInputPasswordToggleButton(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return Matchers.allOf(
                    ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                    Matchers.instanceOf(TextInputLayout::class.java)
                )
            }

            override fun perform(uiController: UiController, view: View) {
                (view as TextInputLayout).isEndIconVisible = false
            }

            override fun getDescription(): String {
                return "Password toggle button hidden."
            }
        })
}

/**
 * Click on the start icon on the [TextInputLayout] associated with [viewId].
 */
public fun RobotActions.clickTextInputLayoutStartIcon(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(TextInputLayout::class.java)
            }

            override fun perform(uiController: UiController, view: View) {
                (view as TextInputLayout)
                    .findViewById<CheckableImageButton>(MaterialR.id.text_input_start_icon)
                    .performClick()
            }

            override fun getDescription(): String {
                return "start icon clicked."
            }
        })
}

/**
 * Click on the end icon on the [TextInputLayout] associated with [viewId].
 */
public fun RobotActions.clickTextInputLayoutEndIcon(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(TextInputLayout::class.java)
            }

            override fun perform(uiController: UiController, view: View) {
                (view as TextInputLayout)
                    .findViewById<CheckableImageButton>(MaterialR.id.text_input_end_icon)
                    .performClick()
            }

            override fun getDescription(): String {
                return "end icon clicked."
            }
        })
}

/**
 * Click on the error icon on the [TextInputLayout] associated with [viewId].
 */
public fun RobotActions.clickTextInputLayoutErrorIcon(@IdRes viewId: Int) {
    Espresso.onView(
        CoreMatchers.allOf(
            ViewMatchers.withId(MaterialR.id.text_input_end_icon),
            ViewMatchers.withContentDescription(MaterialR.string.error_icon_content_description),
            ViewMatchers.isDescendantOfA(ViewMatchers.withId(viewId))
        )
    ).perform(ViewActions.click())
}

/**
 * Long click on the start icon on the [TextInputLayout] associated with [viewId].
 */
public fun RobotActions.longClickTextInputLayoutStartIcon(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(TextInputLayout::class.java)
            }

            override fun perform(uiController: UiController, view: View) {
                (view as TextInputLayout)
                    .findViewById<CheckableImageButton>(MaterialR.id.text_input_start_icon)
                    .performLongClick()
            }

            override fun getDescription(): String {
                return "start icon long clicked."
            }
        })
}

/**
 * Long click on the end icon on the [TextInputLayout] associated with [viewId].
 */
public fun RobotActions.longClickTextInputLayoutEndIcon(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(TextInputLayout::class.java)
            }

            override fun perform(uiController: UiController, view: View) {
                (view as TextInputLayout)
                    .findViewById<CheckableImageButton>(MaterialR.id.text_input_end_icon)
                    .performLongClick()
            }

            override fun getDescription(): String {
                return "end icon long clicked."
            }
        })
}

/**
 * Long click on the error icon on the [TextInputLayout] associated with [viewId].
 */
public fun RobotActions.longClickTextInputLayoutErrorIcon(@IdRes viewId: Int) {
    Espresso.onView(
        CoreMatchers.allOf(
            ViewMatchers.withId(MaterialR.id.text_input_end_icon),
            ViewMatchers.withContentDescription(MaterialR.string.error_icon_content_description),
            ViewMatchers.isDescendantOfA(ViewMatchers.withId(viewId))
        )
    ).perform(ViewActions.longClick())
}
