package reactivecircus.blueprint.ui.extension

import android.view.Window
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * Programmatically shows the soft keyboard.
 */
public fun Window.showSoftKeyboard() {
    WindowInsetsControllerCompat(this, decorView.findViewById(android.R.id.content))
        .show(WindowInsetsCompat.Type.ime())
}

/**
 * Programmatically hides the soft keyboard.
 */
public fun Window.hideSoftKeyboard() {
    WindowInsetsControllerCompat(this, decorView.findViewById(android.R.id.content))
        .hide(WindowInsetsCompat.Type.ime())
}
