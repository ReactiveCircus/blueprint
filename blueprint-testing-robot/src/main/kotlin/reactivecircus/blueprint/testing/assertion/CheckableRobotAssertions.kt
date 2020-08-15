package reactivecircus.blueprint.testing.assertion

import android.view.View
import android.widget.RadioGroup
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.hamcrest.core.AllOf
import reactivecircus.blueprint.testing.RobotAssertions
import reactivecircus.blueprint.testing.scrollTo

/**
 * Check if the radio group associated with [radioGroupId]
 * has [buttonTexts] as the labels of the radio buttons.
 */
public fun RobotAssertions.radioGroupHasText(@IdRes radioGroupId: Int, vararg buttonTexts: String) {
    buttonTexts.forEach { buttonText ->
        scrollTo(buttonText)
        Espresso.onView(
            AllOf.allOf(
                ViewMatchers.withParent(
                    AllOf.allOf(ViewMatchers.isDisplayed(), ViewMatchers.withId(radioGroupId))
                ),
                AllOf.allOf(ViewMatchers.isDisplayed(), ViewMatchers.withText(buttonText))
            )
        ).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}

/**
 * Check if the radio group associated with [radioGroupId]
 * has the button with [buttonText] selected.
 */
public fun RobotAssertions.radioButtonSelected(@IdRes radioGroupId: Int, buttonText: String) {
    scrollTo(buttonText)
    Espresso.onView(
        AllOf.allOf(
            ViewMatchers.withParent(
                AllOf.allOf(ViewMatchers.isDisplayed(), ViewMatchers.withId(radioGroupId))
            ),
            AllOf.allOf(ViewMatchers.isDisplayed(), ViewMatchers.withText(buttonText))
        )
    ).check(ViewAssertions.matches(ViewMatchers.isChecked()))
}

/**
 * Check if the radio group associated with [radioGroupId] has some selections.
 */
public fun RobotAssertions.radioGroupHasSelections(@IdRes radioGroupId: Int) {
    Espresso.onView(AllOf.allOf(ViewMatchers.isDisplayed(), ViewMatchers.withId(radioGroupId)))
        .check(RadioGroupAssertion())
}

/**
 * Check if the radio group associated with [radioGroupId] has NO selection.
 */
public fun RobotAssertions.radioGroupHasNoSelections(@IdRes radioGroupId: Int) {
    Espresso.onView(AllOf.allOf(ViewMatchers.isDisplayed(), ViewMatchers.withId(radioGroupId)))
        .check(ViewAssertions.matches(CoreMatchers.not<Any>(RadioGroupAssertion())))
}

/**
 * Check if the checkable view associated with [viewId] is checked.
 */
public fun RobotAssertions.viewChecked(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .check(ViewAssertions.matches(ViewMatchers.isChecked()))
}

/**
 * Check if the checkable view associated with [viewId] is NOT checked.
 */
public fun RobotAssertions.viewNotChecked(@IdRes viewId: Int) {
    Espresso.onView(ViewMatchers.withId(viewId))
        .check(ViewAssertions.matches(ViewMatchers.isNotChecked()))
}

/**
 * A view assertion that checks if a [RadioGroup] has any selection.
 */
private class RadioGroupAssertion : ViewAssertion {

    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        val radioGroup = view as RadioGroup
        MatcherAssert.assertThat(radioGroup.checkedRadioButtonId, Matchers.greaterThanOrEqualTo(0))
    }
}
