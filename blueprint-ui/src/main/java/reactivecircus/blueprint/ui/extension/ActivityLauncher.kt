package reactivecircus.blueprint.ui.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

/**
 * Launches an activity via [Activity.startActivityForResult].
 * @param requestCode - the requestCode to be passed into [Activity.startActivityForResult]
 * @param options - the options bundle to be  passed into [Activity.startActivityForResult]
 */
inline fun <reified T : Activity> Activity.launchActivity(
    requestCode: Int,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivityForResult(intent, requestCode, options)
}

/**
 * Launches an activity via [Activity.startActivity].
 * @param options - the options bundle to be  passed into [Activity.startActivity]
 */
inline fun <reified T : Activity> Context.launchActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent, options)
}

/**
 * Creates a new intent of type [T].
 */
inline fun <reified T : Activity> newIntent(context: Context): Intent =
    Intent(context, T::class.java)
