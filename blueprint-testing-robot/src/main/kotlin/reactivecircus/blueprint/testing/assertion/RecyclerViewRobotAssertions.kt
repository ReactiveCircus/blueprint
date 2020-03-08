package reactivecircus.blueprint.testing.assertion

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.core.AllOf
import org.junit.Assert
import reactivecircus.blueprint.testing.RobotAssertions
import reactivecircus.blueprint.testing.currentActivity
import reactivecircus.blueprint.testing.matcher.withRecyclerView

/**
 * Check if the recycler view associated with [recyclerViewId] has the size of [size].
 */
fun RobotAssertions.recyclerViewHasSize(@IdRes recyclerViewId: Int, size: Int) {
    Espresso.onView(
        AllOf.allOf<View>(
            ViewMatchers.withId(recyclerViewId),
            ViewMatchers.isDisplayed()
        )
    )
        .check(RecyclerViewItemCountAssertion(size))
}

/**
 * Check if the recycler view associated with [viewIds]
 * are displayed within the view associated with [recyclerViewId].
 */
fun RobotAssertions.viewDisplayedInRecyclerView(@IdRes recyclerViewId: Int, @IdRes vararg viewIds: Int) {
    val recyclerView =
        requireNotNull(currentActivity()).findViewById(recyclerViewId) as RecyclerView
    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
    val visibleCount = layoutManager.findLastVisibleItemPosition() + 1

    Assert.assertTrue(visibleCount > 0)
    (0 until visibleCount).forEach { index ->
        // scroll to the item to make sure it's visible
        Espresso.onView(
            AllOf.allOf(
                ViewMatchers.withId(recyclerViewId),
                ViewMatchers.isDisplayed()
            )
        )
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(index))

        viewIds.forEach { viewId ->
            Espresso.onView(
                withRecyclerView(recyclerViewId)
                    .atPositionOnView(index, viewId)
            )
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }
}

/**
 * A view assertion that checks if a [RecyclerView] has the expected number of items.
 */
private class RecyclerViewItemCountAssertion(private val expectedCount: Int) : ViewAssertion {

    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }
        if (view !is RecyclerView) {
            throw IllegalStateException("The asserted view is not RecyclerView")
        }

        if (view.adapter == null) {
            throw IllegalStateException("No adapter is assigned to RecyclerView")
        }

        val adapter = view.adapter
        MatcherAssert.assertThat(adapter?.itemCount, CoreMatchers.equalTo(expectedCount))
    }
}
