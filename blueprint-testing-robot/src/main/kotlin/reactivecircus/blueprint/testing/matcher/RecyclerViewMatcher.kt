@file:Suppress("ReturnCount")

package reactivecircus.blueprint.testing.matcher

import android.content.res.Resources
import android.view.View
import android.view.ViewParent
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * Returns a matcher that matches a view in the [RecyclerView] at certain position.
 */
fun withRecyclerView(@IdRes recyclerViewId: Int): RecyclerViewMatcher {
    return RecyclerViewMatcher(recyclerViewId)
}

class RecyclerViewMatcher(private val recyclerViewId: Int) {

    fun atPosition(position: Int): Matcher<View> {
        return atPositionOnView(position, -1)
    }

    fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            var resources: Resources? = null
            var itemView: View? = null

            override fun describeTo(description: Description) {
                var idDescription = recyclerViewId.toString()
                if (resources != null) {
                    idDescription = try {
                        requireNotNull(resources).getResourceName(recyclerViewId)
                    } catch (var4: Resources.NotFoundException) {
                        String.format("%s (resource name not found)", recyclerViewId)
                    }
                }
                description.appendText("with id: $idDescription")
            }

            public override fun matchesSafely(view: View): Boolean {
                resources = view.resources

                // only try to match views which are within descendant of RecyclerView
                if (!isDescendantOfA(withId(recyclerViewId)).matches(view)) {
                    return false
                }

                if (itemView == null) {
                    val recyclerView = findParentRecursively(view, recyclerViewId) as RecyclerView?
                    if (recyclerView != null && recyclerView.id == recyclerViewId) {
                        itemView = recyclerView.findViewHolderForAdapterPosition(position)?.itemView
                    } else {
                        return false
                    }
                }

                return if (targetViewId == -1) {
                    view === itemView
                } else {
                    val targetView = itemView?.findViewById<View>(targetViewId)
                    view === targetView
                }
            }

            private fun findParentRecursively(view: View, targetId: Int): ViewParent? {
                if (view.id == targetId) {
                    return view as ViewParent
                }
                val parent = view.parent as View
                return findParentRecursively(parent, targetId)
            }
        }
    }
}
