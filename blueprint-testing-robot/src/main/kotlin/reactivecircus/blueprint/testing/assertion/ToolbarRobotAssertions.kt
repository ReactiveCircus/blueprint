package reactivecircus.blueprint.testing.assertion

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.core.Is
import reactivecircus.blueprint.testing.RobotAssertions

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
                        ApplicationProvider.getApplicationContext<Context>().getString(
                            titleTextResId
                        )
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
                        ApplicationProvider.getApplicationContext<Context>().getString(
                            subtitleTextResId
                        )
                    )
                )
            )
        )
}

/**
 * Returns a matcher that matches the title in [Toolbar].
 */
private fun withToolbarTitle(textMatcher: Matcher<String>): Matcher<Any> {
    return object : BoundedMatcher<Any, Toolbar>(Toolbar::class.java) {
        public override fun matchesSafely(toolbar: Toolbar): Boolean {
            return textMatcher.matches(toolbar.title.toString())
        }

        override fun describeTo(description: Description) {
            description.appendText("with toolbar title: ")
            textMatcher.describeTo(description)
        }
    }
}

/**
 * Returns a matcher that matches the subtitle in [Toolbar].
 */
private fun withToolbarSubtitle(textMatcher: Matcher<CharSequence>): Matcher<Any> {
    return object : BoundedMatcher<Any, Toolbar>(Toolbar::class.java) {
        public override fun matchesSafely(toolbar: Toolbar): Boolean {
            return textMatcher.matches(toolbar.subtitle.toString())
        }

        override fun describeTo(description: Description) {
            description.appendText("with toolbar subtitle: ")
            textMatcher.describeTo(description)
        }
    }
}
