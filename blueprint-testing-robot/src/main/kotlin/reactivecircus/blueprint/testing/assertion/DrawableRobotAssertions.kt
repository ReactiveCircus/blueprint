package reactivecircus.blueprint.testing.assertion

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import reactivecircus.blueprint.testing.RobotAssertions

/**
 * Check if the drawable associated with [resourceId] is displayed.
 */
public fun RobotAssertions.drawableDisplayed(@IdRes resourceId: Int) {
    Espresso.onView(withDrawable(resourceId))
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
}

/**
 * Check if the background drawable associated with [resourceId] is displayed.
 */
public fun RobotAssertions.backgroundDrawableDisplayed(@IdRes resourceId: Int) {
    Espresso.onView(withBackgroundDrawable(resourceId))
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
}

/**
 * Check if the foreground drawable associated with [resourceId] is displayed.
 */
public fun RobotAssertions.foregroundDrawableDisplayed(@IdRes resourceId: Int) {
    Espresso.onView(withForegroundDrawable(resourceId))
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
}

/**
 * Returns a matcher that matches the drawable associated with the [resourceId].
 */
private fun withDrawable(@IdRes resourceId: Int): Matcher<View> {
    return DrawableMatcher(DrawableMatcher.Type.DRAWABLE, resourceId)
}

/**
 * Returns a matcher that matches the background drawable associated with the [resourceId].
 */
private fun withBackgroundDrawable(@IdRes resourceId: Int): Matcher<View> {
    return DrawableMatcher(DrawableMatcher.Type.BACKGROUND_DRAWABLE, resourceId)
}

/**
 * Returns a matcher that matches the foreground drawable associated with the [resourceId].
 */
private fun withForegroundDrawable(@IdRes resourceId: Int): Matcher<View> {
    return DrawableMatcher(DrawableMatcher.Type.FOREGROUND_DRAWABLE, resourceId)
}

private class DrawableMatcher(
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
    @Suppress("ReturnCount", "ComplexMethod")
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
