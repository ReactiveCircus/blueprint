package reactivecircus.blueprint.testing.matcher

import android.content.res.Resources
import android.view.View
import android.view.ViewParent
import androidx.annotation.IdRes
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import java.util.Locale

/**
 * Returns a matcher that matches a view in the [RecyclerView] at certain position.
 */
public fun withRecyclerView(@IdRes recyclerViewId: Int): RecyclerViewMatcher {
    return RecyclerViewMatcher(recyclerViewId)
}

public class RecyclerViewMatcher(@PublishedApi internal val recyclerViewId: Int) {

    public fun atPosition(position: Int): Matcher<View> {
        return atPositionOnView(position, -1)
    }

    public fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            var resources: Resources? = null
            var itemView: View? = null

            override fun describeTo(description: Description) {
                var idDescription = recyclerViewId.toString()
                if (resources != null) {
                    idDescription = runCatching {
                        requireNotNull(resources).getResourceName(recyclerViewId)
                    }.getOrElse {
                        String.format(
                            Locale.getDefault(),
                            "%s (resource name not found)",
                            recyclerViewId
                        )
                    }
                }
                description.appendText("with id: $idDescription")
            }

            @Suppress("ReturnCount")
            override fun matchesSafely(view: View): Boolean {
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
        }
    }

    public fun withItemViewType(
        @IdRes recyclerViewId: Int,
        itemViewType: Int,
    ): Matcher<View> {
        return withItemViewType(recyclerViewId, itemViewType, -1)
    }

    public fun withItemViewType(
        @IdRes recyclerViewId: Int,
        itemViewType: Int,
        targetViewId: Int,
    ): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            var resources: Resources? = null
            var itemView: View? = null

            override fun describeTo(description: Description) {
                var idDescription = recyclerViewId.toString()
                if (resources != null) {
                    idDescription = runCatching {
                        requireNotNull(resources).getResourceName(recyclerViewId)
                    }.getOrElse {
                        String.format(
                            Locale.getDefault(),
                            "%s (resource name not found)",
                            recyclerViewId
                        )
                    }
                }
                description.appendText("with id: $idDescription")
            }

            @Suppress("ReturnCount")
            override fun matchesSafely(view: View): Boolean {
                resources = view.resources

                // only try to match views which are within descendant of RecyclerView
                if (!isDescendantOfA(withId(recyclerViewId)).matches(view)) {
                    return false
                }

                if (itemView == null) {
                    val recyclerView = findParentRecursively(view, recyclerViewId) as RecyclerView?
                    if (recyclerView != null && recyclerView.id == recyclerViewId) {
                        itemView = recyclerView.children.firstOrNull {
                            recyclerView.getChildViewHolder(it).itemViewType == itemViewType
                        }
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
        }
    }
}

@PublishedApi
internal fun findParentRecursively(view: View, targetId: Int): ViewParent? {
    if (view.id == targetId) {
        return view as ViewParent
    }
    val parent = view.parent as View
    return findParentRecursively(parent, targetId)
}
