package reactivecircus.blueprint.testing.assertion

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.core.Is
import reactivecircus.blueprint.testing.RobotAssertions
import reactivecircus.blueprint.testing.withToolbarSubtitle
import reactivecircus.blueprint.testing.withToolbarTitle

/**
 * Check if the current [Toolbar] has a title of [title].
 */
fun RobotAssertions.toolbarHasTitle(title: String) {
    Espresso.onView(ViewMatchers.isAssignableFrom(Toolbar::class.java))
        .check(ViewAssertions.matches(withToolbarTitle(Is.`is`(title))))
}

/**
 * Check if the current [Toolbar] has a title of a string associated with [titleTextResId].
 */
fun RobotAssertions.toolbarHasTitle(@StringRes titleTextResId: Int) {
    Espresso.onView(ViewMatchers.isAssignableFrom(Toolbar::class.java))
        .check(
            ViewAssertions.matches(
                withToolbarTitle(
                    Is.`is`(
                        ApplicationProvider.getApplicationContext<Context>().getString(titleTextResId)
                    )
                )
            )
        )
}

/**
 * Check if the current [Toolbar] has a subtitle of [subtitle].
 */
fun RobotAssertions.toolbarHasSubtitle(subtitle: String) {
    Espresso.onView(ViewMatchers.isAssignableFrom(Toolbar::class.java))
        .check(ViewAssertions.matches(withToolbarSubtitle(Is.`is`(subtitle))))
}

/**
 * Check if the current [Toolbar]
 * has a subtitle of a string associated with [subtitleTextResId].
 */
fun RobotAssertions.toolbarHasSubtitle(@StringRes subtitleTextResId: Int) {
    Espresso.onView(ViewMatchers.isAssignableFrom(Toolbar::class.java))
        .check(
            ViewAssertions.matches(
                withToolbarSubtitle(
                    Is.`is`(
                        ApplicationProvider.getApplicationContext<Context>().getString(subtitleTextResId)
                    )
                )
            )
        )
}
