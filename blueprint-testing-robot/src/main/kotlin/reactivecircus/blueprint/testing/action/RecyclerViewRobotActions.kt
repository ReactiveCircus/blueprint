package reactivecircus.blueprint.testing.action

import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import reactivecircus.blueprint.testing.RobotActions
import reactivecircus.blueprint.testing.scrollToItemInRecyclerView

/**
 * Click on the item at [position] within the recycler view associated with [recyclerViewId].
 */
fun RobotActions.clickRecyclerViewItem(@IdRes recyclerViewId: Int, position: Int) {
    // scroll to the item to make sure it's visible
    scrollToItemInRecyclerView(recyclerViewId, position)

    Espresso.onView(ViewMatchers.withId(recyclerViewId))
        .perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position, ViewActions.click()
            )
        )
}
