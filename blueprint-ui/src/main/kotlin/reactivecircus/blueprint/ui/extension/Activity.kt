package reactivecircus.blueprint.ui.extension

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

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
 * Returns screen size of the activity.
 */
val Activity.screenSize: DisplayMetrics
    get() {
        val display = windowManager.defaultDisplay
        val metrics = DisplayMetrics()
        display.getRealMetrics(metrics)
        return metrics
    }
