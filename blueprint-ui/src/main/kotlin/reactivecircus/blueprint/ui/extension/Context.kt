package reactivecircus.blueprint.ui.extension

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.provider.Settings
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.DrawableCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

/**
 * Apply tinting to a vector drawable.
 */
public fun Context.tintVectorDrawable(
    theme: Resources.Theme,
    @DrawableRes resId: Int,
    @ColorInt tint: Int,
): Drawable {
    val drawable: Drawable = VectorDrawableCompat.create(resources, resId, theme) as Drawable
    val wrapped = DrawableCompat.wrap(drawable)
    drawable.mutate()
    DrawableCompat.setTint(wrapped, tint)
    return drawable
}

/**
 * Whether animation is turned on on the device.
 */
public val Context.isAnimationOn: Boolean
    get() = Settings.Global.getFloat(
        contentResolver,
        Settings.Global.ANIMATOR_DURATION_SCALE,
        1f
    ) > 0
