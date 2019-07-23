package reactivecircus.blueprint.ui.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.provider.Settings
import android.util.AttributeSet
import android.widget.ProgressBar

/**
 * A UI test friendly implementation of an indeterminate ProgressBar,
 * which hides the indeterminate drawable if animation is off
 */
class IndeterminateProgressBar @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : ProgressBar(context, attrs) {

    private val isAnimationOff: Boolean
        get() = Settings.Global.getFloat(
            context.contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE,
            1f
        ) == 0f

    override fun setIndeterminateDrawable(drawable: Drawable) {
        super.setIndeterminateDrawable(if (isAnimationOff) null else drawable)
    }
}
