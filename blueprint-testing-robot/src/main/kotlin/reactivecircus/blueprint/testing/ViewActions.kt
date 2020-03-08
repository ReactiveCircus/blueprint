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
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.core.AllOf

private const val MAX_SCROLL_ATTEMPTS = 100

/**
 * Scroll until the view associated with [viewId] is visible.
 */
fun scrollTo(@IdRes viewId: Int) {
    ViewActions.repeatedlyUntil(
        ViewActions.scrollTo(),
        Matchers.allOf(ViewMatchers.withId(viewId), ViewMatchers.isDisplayed()),
        MAX_SCROLL_ATTEMPTS
    )
}

/**
 * Scroll until the [text]] is visible.
 */
fun scrollTo(text: String) {
    ViewActions.repeatedlyUntil(
        ViewActions.scrollTo(),
        Matchers.allOf(ViewMatchers.withText(text), ViewMatchers.isDisplayed()),
        MAX_SCROLL_ATTEMPTS
    )
}

/**
 * Scroll to the item at [itemIndex] in the [RecyclerView] associated with [recyclerViewId].
 */
fun scrollToItemInRecyclerView(@IdRes recyclerViewId: Int, itemIndex: Int) {
    Espresso.onView(AllOf.allOf(ViewMatchers.withId(recyclerViewId), ViewMatchers.isDisplayed()))
        .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(itemIndex))
}

private const val RECYCLER_VIEW_PENDING_UPDATES_CHECK_INTERVAL_MILLIS = 10L

/**
 * Wait until the [RecyclerView] has no more pending updates.
 */
fun onRecyclerViewIdle(@IdRes recyclerViewId: Int) {
    Espresso.onIdle()

    val recyclerView =
        requireNotNull(currentActivity()).findViewById(recyclerViewId) as RecyclerView

    while (recyclerView.hasPendingAdapterUpdates()) {
        runBlocking {
            delay(RECYCLER_VIEW_PENDING_UPDATES_CHECK_INTERVAL_MILLIS)
        }
    }
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
