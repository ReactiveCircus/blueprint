package reactivecircus.blueprint.ui.extension

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.provider.Settings
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

/**
 * Apply tinting to a vector drawable.
 */
fun Context.tintVectorDrawable(
    theme: Resources.Theme,
    @DrawableRes resId: Int,
    @ColorInt tint: Int
): Drawable {
    val drawable: Drawable = VectorDrawableCompat.create(resources, resId, theme) as Drawable
    val wrapped = DrawableCompat.wrap(drawable)
    drawable.mutate()
    DrawableCompat.setTint(wrapped, tint)
    return drawable
}

/**
 * Resolves the given color attribute and returns the resource ID associated with the color.
 */
@ColorInt
fun Context.resolveColorAttr(@AttrRes colorAttr: Int): Int {
    val resolvedAttr = TypedValue()
    theme.resolveAttribute(colorAttr, resolvedAttr, true)
    // resourceId is used if it's a ColorStateList, and data if it's a color reference or a hex color
    val colorRes = if (resolvedAttr.resourceId != 0) resolvedAttr.resourceId else resolvedAttr.data
    return ContextCompat.getColor(this, colorRes)
}

/**
 * Whether animation is turned on on the device.
 */
val Context.isAnimationOn: Boolean
    get() = Settings.Global.getFloat(
        contentResolver,
        Settings.Global.ANIMATOR_DURATION_SCALE,
        1f
    ) > 0
