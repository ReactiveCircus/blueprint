package reactivecircus.blueprint.testing.assertion

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.CoreMatchers
import org.hamcrest.core.AllOf.allOf
import reactivecircus.blueprint.testing.RobotAssertions

/**
 * Check if the recycler view associated with [recyclerViewId] has the size of [size].
 */
fun RobotAssertions.recyclerViewHasSize(@IdRes recyclerViewId: Int, size: Int) {
    Espresso.onView(
            allOf(
                ViewMatchers.withId(recyclerViewId),
                ViewMatchers.isDisplayed()
            )
        )
        .check(RecyclerViewItemCountAssertion(size))
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

        assertThat(view.adapter?.itemCount, CoreMatchers.equalTo(expectedCount))
    }
}
