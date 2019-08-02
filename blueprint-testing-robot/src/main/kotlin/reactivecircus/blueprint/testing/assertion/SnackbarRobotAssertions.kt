package reactivecircus.blueprint.testing.assertion

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.google.android.material.R
import org.hamcrest.CoreMatchers
import reactivecircus.blueprint.testing.RobotAssertions

/**
 * Check if a snackbar with [text] as message is displayed.
 */
fun RobotAssertions.snackBarDisplayed(text: String) {
    Espresso.onView(
        CoreMatchers.allOf(
            ViewMatchers.withId(R.id.snackbar_text),
            ViewMatchers.withText(text)
        )
    )
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
}
