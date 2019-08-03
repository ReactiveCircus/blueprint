@file:Suppress("TooManyFunctions", "ReturnCount")

package reactivecircus.blueprint.testing

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.InputType
import android.view.View
import android.view.ViewParent
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import java.util.Locale

/**
 * Returns a matcher that matches a [TextInputLayout] with no error.
 */
fun noTextInputLayoutError(): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
        public override fun matchesSafely(view: View): Boolean {
            return view is TextInputLayout && !view.isErrorEnabled
        }

        override fun describeTo(description: Description) {
            description.appendText("no error.")
        }
    }
}

/**
 * Returns a matcher that matches a [TextInputLayout] with a string associated with [errorMessageResId] as error.
 */
fun hasTextInputLayoutErrorText(@StringRes errorMessageResId: Int): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
        public override fun matchesSafely(view: View): Boolean {
            if (view !is TextInputLayout || view.error == null) {
                return false
            }

            val hint = view.error.toString()

            return view.getResources().getString(errorMessageResId) == hint
        }

        override fun describeTo(description: Description) {
            description.appendText("with error string resource id: $errorMessageResId")
        }
    }
}

/**
 * Returns a matcher that matches a [TextInputLayout] with the [expectedErrorText] as error.
 */
fun hasTextInputLayoutErrorText(expectedErrorText: String): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
        public override fun matchesSafely(view: View): Boolean {
            if (view !is TextInputLayout || view.error == null) {
                return false
            }

            val hint = view.error.toString()

            return expectedErrorText == hint
        }

        override fun describeTo(description: Description) {
            description.appendText("with error: $expectedErrorText")
        }
    }
}

/**
 * Returns a matcher that matches a [BottomNavigationView] with the item associated with [itemResId] selected.
 */
fun hasSelectedNavigationItem(@IdRes itemResId: Int): Matcher<View> {
    return object : BoundedMatcher<View, BottomNavigationView>(BottomNavigationView::class.java) {
        private val checkedIds = hashSetOf<Int>()
        private var itemFound = false
        private var triedMatching = false

        override fun matchesSafely(bottomNavigationView: BottomNavigationView): Boolean {
            triedMatching = true
            with(bottomNavigationView.menu) {
                for (index in 0 until size()) {
                    val menuItem = getItem(index)
                    if (menuItem.isChecked) {
                        checkedIds.add(menuItem.itemId)
                    }
                    if (menuItem.itemId == itemResId) {
                        itemFound = true
                    }
                }
            }
            return checkedIds.contains(itemResId)
        }

        override fun describeTo(description: Description) {
            if (!triedMatching) {
                description.appendText("BottomNavigationView")
                return
            }

            description.appendText("BottomNavigationView to have a checked item with id=")
            description.appendValue(itemResId)
            if (itemFound) {
                description.appendText(", but selection was=")
                description.appendValue(checkedIds)
            } else {
                description.appendText(", but it doesn't have an item with such id")
            }
        }
    }
}

/**
 * Returns a matcher that matches the [EditText] with the Email input type.
 */
fun withEmailInputType(): Matcher<View> {
    return object : BoundedMatcher<View, EditText>(EditText::class.java) {

        override fun matchesSafely(item: EditText): Boolean {
            return item.inputType and InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS > 0
        }

        override fun describeTo(description: Description) {
            description.appendText("with error: The input type of the EditText is not Email Address input type.")
        }
    }
}

/**
 * Returns a matcher that matches the navigation button in [Toolbar].
 */
fun withToolbarNavigationButton(): Matcher<View> {
    return allOf(
        withParent(withClassName(`is`(Toolbar::class.java.name))),
        withClassName(
            anyOf(
                `is`(ImageButton::class.java.name),
                `is`(AppCompatImageButton::class.java.name)
            )
        )
    )
}

/**
 * Returns a matcher that matches the title in [Toolbar].
 */
fun withToolbarTitle(textMatcher: Matcher<String>): Matcher<Any> {
    return object : BoundedMatcher<Any, Toolbar>(Toolbar::class.java) {
        public override fun matchesSafely(toolbar: Toolbar): Boolean {
            return textMatcher.matches(toolbar.title.toString())
        }

        override fun describeTo(description: Description) {
            description.appendText("with toolbar title: ")
            textMatcher.describeTo(description)
        }
    }
}

/**
 * Returns a matcher that matches the subtitle in [Toolbar].
 */
fun withToolbarSubtitle(textMatcher: Matcher<CharSequence>): Matcher<Any> {
    return object : BoundedMatcher<Any, Toolbar>(Toolbar::class.java) {
        public override fun matchesSafely(toolbar: Toolbar): Boolean {
            return textMatcher.matches(toolbar.subtitle.toString())
        }

        override fun describeTo(description: Description) {
            description.appendText("with toolbar subtitle: ")
            textMatcher.describeTo(description)
        }
    }
}

/**
 * Returns a matcher that matches string containing a subString (case insensitive).
 */
fun containsIgnoringCase(subString: String): Matcher<String> {

    return object : TypeSafeMatcher<String>() {
        override fun matchesSafely(actualString: String): Boolean {
            return actualString.toLowerCase(Locale.getDefault())
                .contains(subString.toLowerCase(Locale.getDefault()))
        }

        override fun describeTo(description: Description) {
            description.appendText("containing substring \"$subString\"")
        }
    }
}

/**
 * Returns a matcher that matches the drawable associated with the [resourceId].
 */
fun withDrawable(@IdRes resourceId: Int): Matcher<View> {
    return DrawableMatcher(DrawableMatcher.Type.DRAWABLE, resourceId)
}

/**
 * Returns a matcher that matches the background drawable associated with the [resourceId].
 */
fun withBackgroundDrawable(@IdRes resourceId: Int): Matcher<View> {
    return DrawableMatcher(DrawableMatcher.Type.BACKGROUND_DRAWABLE, resourceId)
}

/**
 * Returns a matcher that matches the foreground drawable associated with the [resourceId].
 */
fun withForegroundDrawable(@IdRes resourceId: Int): Matcher<View> {
    return DrawableMatcher(DrawableMatcher.Type.FOREGROUND_DRAWABLE, resourceId)
}

class DrawableMatcher(
    private val type: Type,
    private val expectedId: Int
) : TypeSafeMatcher<View>(View::class.java) {

    private var resourceName: String? = null

    enum class Type {
        DRAWABLE,
        BACKGROUND_DRAWABLE,
        FOREGROUND_DRAWABLE
    }

    @SuppressLint("NewApi")
    override fun matchesSafely(target: View): Boolean {
        if (expectedId < 0) {
            return false
        }

        val resources = target.context.resources
        val expectedDrawable = ContextCompat.getDrawable(target.context, expectedId)
        resourceName = resources.getResourceEntryName(expectedId)

        if (expectedDrawable == null) {
            return false
        }

        val constantState: Drawable.ConstantState?
        val expectedConstantState: Drawable.ConstantState?

        when (type) {
            Type.DRAWABLE -> {
                if (target !is ImageView || target.drawable == null) {
                    return false
                }
                constantState = target.drawable.constantState
                expectedConstantState = expectedDrawable.constantState
                return constantState != null &&
                        expectedConstantState != null &&
                        constantState == expectedConstantState ||
                        getBitmap(target.drawable).sameAs(getBitmap(expectedDrawable))
            }
            Type.BACKGROUND_DRAWABLE -> {
                constantState = target.background.constantState
                expectedConstantState = expectedDrawable.constantState
                return constantState != null &&
                        expectedConstantState != null &&
                        constantState == expectedConstantState ||
                        getBitmap(target.background).sameAs(getBitmap(expectedDrawable))
            }
            Type.FOREGROUND_DRAWABLE -> {
                constantState = target.foreground.constantState
                expectedConstantState = expectedDrawable.constantState
                return constantState != null &&
                        expectedConstantState != null &&
                        constantState == expectedConstantState ||
                        getBitmap(target.foreground).sameAs(getBitmap(expectedDrawable))
            }
        }
    }

    override fun describeTo(description: Description) {
        description.appendText("with drawable from resource id: ")
        description.appendValue(expectedId)
        if (resourceName != null) {
            description.appendText("[")
            description.appendText(resourceName)
            description.appendText("]")
        }
    }

    private fun getBitmap(drawable: Drawable): Bitmap {
        val result: Bitmap
        if (drawable is BitmapDrawable) {
            result = drawable.bitmap
        } else {
            var width = drawable.intrinsicWidth
            var height = drawable.intrinsicHeight
            // some drawables have no intrinsic width - e.g. solid colours.
            if (width <= 0) {
                width = 1
            }
            if (height <= 0) {
                height = 1
            }

            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(result)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
        }
        return result
    }
}

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
