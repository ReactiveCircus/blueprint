# Blueprint UI

The **Blueprint UI** provides a number of convenient Kotlin extensions and widgets for working with the Android UI toolkit.

## Dependency

```groovy
implementation "io.github.reactivecircus.blueprint:blueprint-ui:${blueprint_version}"
```

Note that the library uses `androidx.appcompat:appcompat` transitively.

## Extensions

Kotlin extensions on `Activity`:

```Kotlin
/**
 * Programmatically close soft keyboard.
 */
fun Activity.hideKeyboard(focusedView: View)

/**
 * Programmatically show soft keyboard.
 */
fun Activity.showKeyboard()

/**
 * Shows status bar on the activity.
 */
fun Activity.showStatusBar()

/**
 * Hides status bar from the activity.
 */
fun Activity.hideStatusBar()

/**
 * Sets status bar color on the activity and optionally draws the status bar system ui in light or dark mode.
 */
fun Activity.setStatusBarColor(@ColorRes colorRes: Int, lightBackground: Boolean = false)

/**
 * Returns screen size of the activity.
 */
val Activity.screenSize: DisplayMetrics
```

Kotlin extensions on `Context`:

```Kotlin
/**
 * Apply tinting to a vector drawable.
 */
fun Context.tintVectorDrawable(
    theme: Resources.Theme,
    @DrawableRes resId: Int,
    @ColorInt tint: Int
): Drawable

/**
 * Resolves the given color attribute and returns the resource ID associated with the color.
 */
@ColorInt
fun Context.resolveColorAttr(@AttrRes colorAttr: Int): Int

/**
 * Whether animation is turned on on the device.
 */
fun Context.isAnimationOn(): Boolean
```

Kotlin extensions on `AppCompat`:

```Kotlin
/**
 * Sets the precomputed text future on the [AppCompatTextView].
 *
 * @param charSequence the text to be displayed
 * @param executor the executor to be used for processing the text layout.
 *  Default single threaded pool will be used if null is passed in.
 */
fun AppCompatTextView.setPrecomputedTextFuture(charSequence: CharSequence, executor: Executor? = null)
```

[Intent.kt][intent-extensions] has extensions on `Activity` and `Context` for launching new activity.  

For example to launch a new Activity from an Activity:

```kotlin
launchActivity<NoteActivity> {
    putExtra(EXTRA_ENTER_NOTE_ID, 1)
}
```

To launch a new Activity, passing in a **request code**:
```kotlin
launchActivity<ScanQrCodeActivity>(requestCode = SCAN_QR_CODE_REQUEST)
```

This internally launches the activity with `Activity.startActivityForResult(...)`.

## Test-friendly ProgressBar

Sometimes **Espresso** might **continuously** wait for an indeterminate progress bar animation (spinning) to complete even when animations are turned off on device during instrumentation tests, causing a timeout.

The `IndeterminateProgressBar` hides the indeterminate drawable if animation is off. Replace the stock `ProgressBar` with the following:

```xml
<reactivecircus.blueprint.ui.widget.IndeterminateProgressBar/>
```

[intent-extensions]: https://github.com/ReactiveCircus/blueprint/tree/master/blueprint-ui/src/main/kotlin/reactivecircus/blueprint/ui/extension/Intent.kt
