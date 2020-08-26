package reactivecircus.blueprint.testing

import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.core.AllOf

private const val MAX_SCROLL_ATTEMPTS = 100

/**
 * Scroll until the view associated with [viewId] is visible.
 */
public fun scrollTo(@IdRes viewId: Int) {
    ViewActions.repeatedlyUntil(
        ViewActions.scrollTo(),
        Matchers.allOf(ViewMatchers.withId(viewId), isDisplayed()),
        MAX_SCROLL_ATTEMPTS
    )
}

/**
 * Scroll until the [text]] is visible.
 */
public fun scrollTo(text: String) {
    ViewActions.repeatedlyUntil(
        ViewActions.scrollTo(),
        Matchers.allOf(ViewMatchers.withText(text), isDisplayed()),
        MAX_SCROLL_ATTEMPTS
    )
}

/**
 * Scroll to the item at [itemIndex] in the [RecyclerView] associated with [recyclerViewId].
 */
public fun scrollToItemInRecyclerView(@IdRes recyclerViewId: Int, itemIndex: Int) {
    Espresso.onView(AllOf.allOf(ViewMatchers.withId(recyclerViewId), isDisplayed()))
        .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(itemIndex))
}

private const val RECYCLER_VIEW_PENDING_UPDATES_CHECK_INTERVAL_MILLIS = 10L

/**
 * Wait until the [RecyclerView] has no more pending updates.
 */
public fun onRecyclerViewIdle(@IdRes recyclerViewId: Int) {
    Espresso.onIdle()
    Espresso.onView(allOf(ViewMatchers.withId(recyclerViewId), isDisplayed()))
        .perform(
            object : ViewAction {
                override fun getConstraints(): Matcher<View> =
                    allOf(ViewMatchers.withId(recyclerViewId), isDisplayed())

                override fun getDescription(): String =
                    "waiting until RecyclerView has no more pending updates"

                override fun perform(uiController: UiController, view: View) {
                    val recyclerView = view as RecyclerView
                    while (recyclerView.hasPendingAdapterUpdates()) {
                        runBlocking {
                            delay(RECYCLER_VIEW_PENDING_UPDATES_CHECK_INTERVAL_MILLIS)
                        }
                    }
                }
            }
        )
}

/**
 * Clear the scrollFlags on the [Toolbar] associated with [toolbarId].
 */
public fun clearToolbarScrollFlags(@IdRes toolbarId: Int) {
    Espresso.onView(ViewMatchers.withId(toolbarId))
        .perform(
            object : ViewAction {
                override fun getConstraints(): Matcher<View> {
                    return Matchers.allOf(
                        ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                        Matchers.instanceOf(Toolbar::class.java)
                    )
                }

                override fun perform(uiController: UiController, view: View) {
                    val toolbar = (view as Toolbar)
                    val params = (view.layoutParams as AppBarLayout.LayoutParams).apply {
                        scrollFlags = 0
                    }
                    toolbar.layoutParams = params
                }

                override fun getDescription(): String {
                    return "Toolbar scrollFlags cleared."
                }
            }
        )
}

private const val DEFAULT_VIEW_ACTION_DELAY_MILLIS = 200L

/**
 * Convert anything to a [ViewAction] by looping the main thread for at least [delayMillis].
 * This is useful when the trigger of an action isn't a regular Espresso [ViewAction] e.g. some external events,
 * but needs to participate in Espresso's internal synchronisation mechanisms
 * to prevent the next [ViewAction] or [Matcher] from executing too early.
 */
public fun Any.asViewAction(delayMillis: Long = DEFAULT_VIEW_ACTION_DELAY_MILLIS) {
    also {
        Espresso.onView(isRoot())
            .perform(
                object : ViewAction {
                    override fun getConstraints(): Matcher<View> {
                        return isRoot()
                    }

                    override fun getDescription(): String {
                        return "Wait for $delayMillis milliseconds."
                    }

                    override fun perform(uiController: UiController, view: View) {
                        uiController.loopMainThreadForAtLeast(delayMillis)
                    }
                }
            )
    }
}
