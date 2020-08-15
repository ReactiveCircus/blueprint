package reactivecircus.blueprint.testing.action

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.core.AllOf
import reactivecircus.blueprint.testing.RobotActions
import reactivecircus.blueprint.testing.scrollTo

/**
 * Click on the radio button with [buttonText]
 * within the radio group associated with [radioGroupId].
 */
public fun RobotActions.clickRadioButton(@IdRes radioGroupId: Int, buttonText: String) {
    scrollTo(buttonText)
    Espresso.onView(
        AllOf.allOf(
            ViewMatchers.withParent(
                AllOf.allOf(ViewMatchers.isDisplayed(), ViewMatchers.withId(radioGroupId))
            ),
            AllOf.allOf(ViewMatchers.isDisplayed(), ViewMatchers.withText(buttonText))
        )
    ).perform(ViewActions.click())
}

/**
 * Select the checkbox with [text] within the view group associated with [layoutId].
 */
public fun RobotActions.selectCheckBox(@IdRes layoutId: Int, text: String) {
    scrollTo(text)
    Espresso.onView(
        AllOf.allOf(
            ViewMatchers.withParent(
                AllOf.allOf(ViewMatchers.isDisplayed(), ViewMatchers.withId(layoutId))
            ),
            AllOf.allOf(ViewMatchers.isDisplayed(), ViewMatchers.withText(text))
        )
    ).perform(ViewActions.click())
}
