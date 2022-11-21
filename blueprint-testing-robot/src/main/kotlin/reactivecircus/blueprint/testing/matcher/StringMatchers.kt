package reactivecircus.blueprint.testing.matcher

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * Returns a matcher that matches string containing a subString (case insensitive).
 */
public fun containsIgnoringCase(subString: String): Matcher<String> {
    return object : TypeSafeMatcher<String>() {
        override fun matchesSafely(actualString: String): Boolean {
            return actualString.lowercase()
                .contains(subString.lowercase())
        }

        override fun describeTo(description: Description) {
            description.appendText("containing substring \"$subString\"")
        }
    }
}
