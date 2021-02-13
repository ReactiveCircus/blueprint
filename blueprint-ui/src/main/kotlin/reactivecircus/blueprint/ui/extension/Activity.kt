package reactivecircus.blueprint.ui.extension

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * Programmatically close soft keyboard.
 */
public fun Activity.hideKeyboard(focusedView: View) {
    val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(focusedView.windowToken, 0)
}

/**
 * Programmatically show soft keyboard.
 */
public fun Activity.showKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

/**
 * Shows status bar on the activity.
 */
public fun Activity.showStatusBar() {
    WindowInsetsControllerCompat(window, window.decorView.findViewById(android.R.id.content))
        .show(WindowInsetsCompat.Type.statusBars())
}

/**
 * Hides status bar from the activity.
 */
public fun Activity.hideStatusBar() {
    WindowInsetsControllerCompat(window, window.decorView.findViewById(android.R.id.content))
        .hide(WindowInsetsCompat.Type.statusBars())
}

/**
 * Sets status bar color on the activity and optionally draws the status bar system ui in light or dark mode.
 * @param colorRes resource ID of the color to be set to the status bar.
 * @param lightBackground whether to draw the status bar such that
 * it is compatible with a light status bar background.
 */
public fun Activity.setStatusBarColor(
    @ColorRes colorRes: Int,
    lightBackground: Boolean = false,
) {
    window.statusBarColor = ContextCompat.getColor(this, colorRes)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        WindowInsetsControllerCompat(window, window.decorView.findViewById(android.R.id.content))
            .isAppearanceLightStatusBars = lightBackground
    }
}

/**
 * Returns screen size of the activity.
 */
public val Activity.screenSize: DisplayMetrics
    get() {
        val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            display
        } else {
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay
        }
        val metrics = DisplayMetrics()
        display?.getRealMetrics(metrics)
        return metrics
    }
