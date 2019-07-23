package reactivecircus.blueprint.ui.extension

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

/**
 * Programmatically close soft keyboard.
 */
fun Activity.hideKeyboard(focusedView: View) {
    val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(focusedView.windowToken, 0)
}

/**
 * Programmatically show soft keyboard.
 */
fun Activity.showKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

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
 * Shows status bar on the activity.
 */
fun Activity.showStatusBar() {
    val decorView = window.decorView
    val uiOptions = View.SYSTEM_UI_FLAG_VISIBLE
    decorView.systemUiVisibility = uiOptions
}

/**
 * Hides status bar from the activity.
 */
fun Activity.hideStatusBar() {
    val decorView = window.decorView
    val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    decorView.systemUiVisibility = uiOptions
}

/**
 * Sets status bar color on the activity.
 */
fun Activity.setStatusBarColor(@ColorRes colorRes: Int) {
    window.statusBarColor = ContextCompat.getColor(this, colorRes)
}

/**
 * Whether animation is turned on on the device.
 */
fun Context.isAnimationOn(): Boolean {
    return Settings.Global.getFloat(contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) > 0
}

/**
 * Returns screen size of the activity.
 */
val Activity.screenSize: DisplayMetrics
    get() {
        val display = windowManager.defaultDisplay
        val metrics = DisplayMetrics()
        display.getRealMetrics(metrics)
        return metrics
    }
