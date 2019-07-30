package reactivecircus.blueprint.testing

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.core.AllOf

private const val MAX_SCROLL_ATTEMPTS = 100

/**
 * Perform an action that scrolls until view with [id] is visible.
 */
fun scrollTo(@IdRes id: Int) {
    ViewActions.repeatedlyUntil(
        ViewActions.scrollTo(),
        allOf(withId(id), isDisplayed()),
        MAX_SCROLL_ATTEMPTS
    )
}

/**
 * Perform an action that scrolls until [text] is visible.
 */
fun scrollTo(text: String) {
    ViewActions.repeatedlyUntil(
        ViewActions.scrollTo(),
        allOf(withText(text), isDisplayed()),
        MAX_SCROLL_ATTEMPTS
    )
}

/**
 * Perform an action that scrolls to the item at [itemIndex] in a [RecyclerView].
 */
fun scrollToItemInRecyclerView(@IdRes viewId: Int, itemIndex: Int) {
    Espresso.onView(AllOf.allOf(withId(viewId), isDisplayed()))
        .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(itemIndex))
}

/**
 * Perform an action that hides the password toggle button in a [TextInputLayout].
 */
fun hideTextInputPasswordToggleButton(@IdRes viewId: Int) {
    Espresso.onView(withId(viewId))
        .perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return allOf(
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

private const val DEFAULT_VIEW_ACTION_DELAY_MILLIS = 200L

/**
 * Convert anything to a [ViewAction] by looping the main thread for at least [delayMillis].
 * This is useful when the trigger of an action isn't a regular Espresso [ViewAction] e.g. some external events,
 * but needs to participate in Espresso's internal synchronisation mechanisms
 * to prevent the next [ViewAction] or [Matcher] from executing too early.
 */
fun Any.asViewAction(delayMillis: Long = DEFAULT_VIEW_ACTION_DELAY_MILLIS) {
    also {
        Espresso.onView(isRoot())
            .perform(object : ViewAction {
                override fun getConstraints(): Matcher<View> {
                    return isRoot()
                }

                override fun getDescription(): String {
                    return "Wait for $delayMillis milliseconds."
                }

                override fun perform(uiController: UiController, view: View) {
                    uiController.loopMainThreadForAtLeast(delayMillis)
                }
            })
    }
}
