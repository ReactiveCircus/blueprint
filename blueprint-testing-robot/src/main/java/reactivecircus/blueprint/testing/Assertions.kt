package reactivecircus.blueprint.testing

import android.app.Activity
import android.view.View
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers

/**
 * A view assertion that checks if a [RadioGroup] has any selection.
 */
fun radioGroupHasSelections(): RadioGroupAssertion {
    return RadioGroupAssertion()
}

class RadioGroupAssertion : ViewAssertion {

    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        val radioGroup = view as RadioGroup
        MatcherAssert.assertThat(radioGroup.checkedRadioButtonId, Matchers.greaterThanOrEqualTo(0))
    }
}

/**
 * A view assertion that checks if a [RecyclerView] has the expected number of items.
 */
fun recyclerViewHasSize(expectedCount: Int): RecyclerViewItemCountAssertion {
    return RecyclerViewItemCountAssertion(expectedCount)
}

class RecyclerViewItemCountAssertion(private val expectedCount: Int) : ViewAssertion {

    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        MatcherAssert.assertThat(adapter?.itemCount, CoreMatchers.equalTo(expectedCount))
    }
}

/**
 * A view assertion that checks if no activity is currently displayed.
 */
fun activityNotDisplayed(activity: Activity?): ActivityNotDisplayedAssertion {
    return ActivityNotDisplayedAssertion(activity)
}

class ActivityNotDisplayedAssertion(private val activity: Activity?) : ViewAssertion {

    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        activity?.run {
            MatcherAssert.assertThat(isFinishing, CoreMatchers.equalTo(true))
        }
    }
}
