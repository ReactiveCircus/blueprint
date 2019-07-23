@file:Suppress("TooManyFunctions", "LargeClass")

package reactivecircus.blueprint.testing

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.core.AllOf
import org.hamcrest.core.Is
import org.hamcrest.core.StringContains
import org.hamcrest.core.StringStartsWith
import org.junit.Assert

/**
 * Base class for implementing a robot DSL.
 */
abstract class BaseRobot<out A : RobotActions, out S : RobotAssertions>(
    private val robotActions: A,
    private val robotAssertions: S
) {
    fun given(block: () -> Unit) = block()
    fun perform(block: A.() -> Unit) = robotActions.apply { block() }
    fun check(block: S.() -> Unit) = robotAssertions.apply { block() }
}

/**
 * Default robot actions for performing common view actions.
 */
open class RobotActions {

    /**
     * Click on the view associated with [viewId].
     */
    fun clickView(@IdRes viewId: Int) {
        Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.click())
    }

    /**
     * Long-click on the view associated with [viewId].
     */
    fun longClickView(@IdRes viewId: Int) {
        Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.longClick())
    }

    /**
     * Enter [text] into the edit text associated with [viewId].
     */
    fun enterTextIntoView(@IdRes viewId: Int, text: String) {
        Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.typeText(text))
    }

    /**
     * Replace the text in the edit text associated with [viewId] with [text].
     */
    fun replaceTextInView(@IdRes viewId: Int, text: String) {
        Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.replaceText(text))
    }

    /**
     * Clear the text in the edit text associated with [viewId].
     */
    fun clearTextInView(@IdRes viewId: Int) {
        Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.clearText())
    }

    /**
     * Close the soft keyboard.
     */
    fun closeKeyboard(@IdRes viewId: Int) {
        Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.closeSoftKeyboard())
    }

    /**
     * Press the action button on the keyboard.
     */
    fun pressKeyboardActionButton(@IdRes viewId: Int) {
        Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.pressImeActionButton())
    }

    /**
     * Swipe left on the view associated with [viewId].
     */
    fun swipeLeftOnView(@IdRes viewId: Int) {
        Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.swipeLeft())
    }

    /**
     * Swipe right on the view associated with [viewId].
     */
    fun swipeRightOnView(@IdRes viewId: Int) {
        Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.swipeRight())
    }

    /**
     * Swipe up on the view associated with [viewId].
     */
    fun swipeUpOnView(@IdRes viewId: Int) {
        Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.swipeUp())
    }

    /**
     * Swipe up down the view associated with [viewId].
     */
    fun swipeDownOnView(@IdRes viewId: Int) {
        Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.swipeDown())
    }

    /**
     * Click on button 1 of the currently displayed dialog.
     */
    fun clickDialogButton1() {
        Espresso.onView(ViewMatchers.withId(android.R.id.button1)).perform(ViewActions.click())
    }

    /**
     * Click on button 2 of the currently displayed dialog.
     */
    fun clickDialogButton2() {
        Espresso.onView(ViewMatchers.withId(android.R.id.button2)).perform(ViewActions.click())
    }

    /**
     * Click on button 3 of the currently displayed dialog.
     */
    fun clickDialogButton3() {
        Espresso.onView(ViewMatchers.withId(android.R.id.button3)).perform(ViewActions.click())
    }

    /**
     * Click on the item at [position] within the recycler view associated with [recyclerViewId].
     */
    fun clickRecyclerViewItem(@IdRes recyclerViewId: Int, position: Int) {
        // scroll to the item to make sure it's visible
        scrollToItemInRecyclerView(recyclerViewId, position)

        Espresso.onView(ViewMatchers.withId(recyclerViewId))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    position, ViewActions.click()
                )
            )
    }

    /**
     * Click on the radio button with [buttonText]
     * within the radio group associated with [radioGroupId].
     */
    fun clickRadioButton(@IdRes radioGroupId: Int, buttonText: String) {
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
    fun selectCheckBox(@IdRes layoutId: Int, text: String) {
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

    /**
     * Select the bottom navigation item with [navItemTitle]
     * from the bottom navigation view associated with [bottomNavigationViewResId].
     */
    fun selectBottomNavigationItem(@IdRes bottomNavigationViewResId: Int, navItemTitle: String) {
        Espresso.onView(
            allOf(
                ViewMatchers.withId(com.google.android.material.R.id.icon),
                ViewMatchers.hasSibling(
                    ViewMatchers.hasDescendant(
                        ViewMatchers.withText(
                            navItemTitle
                        )
                    )
                ),
                ViewMatchers.isDescendantOfA(ViewMatchers.withId(bottomNavigationViewResId))
            )
        )
            .perform(ViewActions.click())
    }

    /**
     * Press the Android back button.
     */
    fun pressBack() {
        Espresso.pressBackUnconditionally()
    }

    /**
     * Click the navigation up button in the current toolbar.
     */
    fun clickNavigateUpButton() {
        Espresso.onView(withToolbarNavigationButton()).perform(ViewActions.click())
    }

    /**
     * Open the drawer associated with [drawerId].
     */
    fun openDrawer(@IdRes drawerId: Int) {
        Espresso.onView(ViewMatchers.withId(drawerId)).perform(DrawerActions.open())
    }

    /**
     * Close the drawer associated with [drawerId].
     */
    fun closeDrawer(@IdRes drawerId: Int) {
        Espresso.onView(ViewMatchers.withId(drawerId)).perform(DrawerActions.close())
    }

    /**
     * Intercept the future intent and respond with [Activity.RESULT_OK].
     */
    fun interceptIntents() {
        Intents.intending(not(IntentMatchers.isInternal()))
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }
}

/**
 * Default robot assertions for performing common view assertions.
 */
open class RobotAssertions {

    /**
     * Check if all views associated with [viewIds] are displayed.
     */
    fun viewDisplayed(@IdRes vararg viewIds: Int) {
        viewIds.forEach { viewId ->
            Espresso.onView(ViewMatchers.withId(viewId))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }

    /**
     * Check if all views associated with [viewIds] are NOT displayed.
     */
    fun viewNotDisplayed(@IdRes vararg viewIds: Int) {
        viewIds.forEach { viewId ->
            Espresso.onView(ViewMatchers.withId(viewId))
                .check(ViewAssertions.matches(not<View>(ViewMatchers.isDisplayed())))
        }
    }

    /**
     * Check if all texts associated with [textResIds] are displayed.
     */
    fun textDisplayed(@StringRes vararg textResIds: Int) {
        textResIds.forEach { textResId ->
            Espresso.onView(ViewMatchers.withText(textResId))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }

    /**
     * Check if all [texts] are displayed.
     */
    fun textDisplayed(vararg texts: String) {
        texts.forEach { text ->
            Espresso.onView(ViewMatchers.withText(text))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }

    /**
     * Check if no views associated with [textResIds] is displayed.
     */
    fun textNotDisplayed(@StringRes vararg textResIds: Int) {
        textResIds.forEach { textResId ->
            Espresso.onView(ViewMatchers.withText(textResId))
                .check(ViewAssertions.doesNotExist())
        }
    }

    /**
     * Check if none of [texts] is displayed.
     */
    fun textNotDisplayed(vararg texts: String) {
        texts.forEach { text ->
            Espresso.onView(ViewMatchers.withText(text))
                .check(ViewAssertions.doesNotExist())
        }
    }

    /**
     * Check if the view associated with [viewId] has [expected] text.
     */
    fun viewHasText(@IdRes viewId: Int, expected: String) {
        Espresso.onView(ViewMatchers.withId(viewId))
            .check(ViewAssertions.matches(ViewMatchers.withText(expected)))
    }

    /**
     * Check if the view associated with [viewId] has string associated with [messageResId] text.
     */
    fun viewHasText(@IdRes viewId: Int, @StringRes messageResId: Int) {
        Espresso.onView(ViewMatchers.withId(viewId))
            .check(ViewAssertions.matches(ViewMatchers.withText(messageResId)))
    }

    /**
     * Check if any descendant views of the view associated with [viewId] has [expected] string.
     */
    fun viewContainsText(@IdRes viewId: Int, expected: String) {
        Espresso.onView(ViewMatchers.withId(viewId))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.withText(
                        StringContains.containsString(
                            expected
                        )
                    )
                )
            )
    }

    /**
     * Check if the view associated with [parentResId] has text that starts with [expected].
     */
    fun viewStartsWithText(expected: String, @IdRes parentResId: Int) {
        Espresso.onView(
            AllOf.allOf<View>(
                ViewMatchers.withText(StringStartsWith.startsWith(expected)),
                ViewMatchers.isDescendantOfA(ViewMatchers.withId(parentResId))
            )
        )
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * Check if the view associated with [viewId]
     * has a hint that equals to the string associated with [messageResId].
     */
    fun viewHasHint(@IdRes viewId: Int, @StringRes messageResId: Int) {
        Espresso.onView(ViewMatchers.withId(viewId))
            .check(ViewAssertions.matches(ViewMatchers.withHint(messageResId)))
    }

    /**
     * Check if text input layout associated with [viewId] has [errorMessage] as the error.
     */
    fun textInputLayoutHasError(@IdRes viewId: Int, errorMessage: String) {
        Espresso.onView(ViewMatchers.withId(viewId))
            .check(ViewAssertions.matches(hasTextInputLayoutErrorText(errorMessage)))
    }

    /**
     * Check if text input layout associated with [viewId]
     * has string associated with [errorMessageResId] as the error.
     */
    fun textInputLayoutHasError(@IdRes viewId: Int, @StringRes errorMessageResId: Int) {
        Espresso.onView(ViewMatchers.withId(viewId))
            .check(ViewAssertions.matches(hasTextInputLayoutErrorText(errorMessageResId)))
    }

    /**
     * Check if the text input layout associated with [viewId] has NO error.
     */
    fun textInputLayoutHasNoError(@IdRes viewId: Int) {
        Espresso.onView(ViewMatchers.withId(viewId))
            .check(ViewAssertions.matches(noTextInputLayoutError()))
    }

    /**
     * Check if the edit text associated with [viewId] has an email input type.
     */
    fun keyboardInputTypeIsEmail(@IdRes viewId: Int) {
        Espresso.onView(ViewMatchers.withId(viewId))
            .check(ViewAssertions.matches(withEmailInputType()))
    }

    /**
     * Check if the view associated with [viewId] is enabled.
     */
    fun viewEnabled(@IdRes viewId: Int) {
        Espresso.onView(ViewMatchers.withId(viewId))
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
    }

    /**
     * Check if the view associated with [viewId] is disabled.
     */
    fun viewDisabled(@IdRes viewId: Int) {
        Espresso.onView(ViewMatchers.withId(viewId))
            .check(ViewAssertions.matches(not<View>(ViewMatchers.isEnabled())))
    }

    /**
     * Check if the view associated with [viewId] is clickable.
     */
    fun viewClickable(@IdRes viewId: Int) {
        Espresso.onView(ViewMatchers.withId(viewId))
            .check(ViewAssertions.matches(ViewMatchers.isClickable()))
    }

    /**
     * Check if the view associated with [viewId] is NOT clickable.
     */
    fun viewNotClickable(@IdRes viewId: Int) {
        Espresso.onView(ViewMatchers.withId(viewId))
            .check(ViewAssertions.matches(not<View>(ViewMatchers.isClickable())))
    }

    /**
     * Check if a dialog with the title of a string associated with [titleResId] is displayed.
     */
    fun dialogWithTextDisplayed(@StringRes titleResId: Int) {
        Espresso.onView(ViewMatchers.withText(titleResId))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * Check if a dialog with the title [expected] is displayed.
     */
    fun dialogWithTextDisplayed(expected: String) {
        Espresso.onView(ViewMatchers.withText(expected))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * Check if a dialog with [buttonTextResId] as the button 1 label is displayed.
     */
    fun dialogWithButton1Displayed(@StringRes buttonTextResId: Int) {
        Espresso.onView(ViewMatchers.withId(android.R.id.button1))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.withText(buttonTextResId)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * Check if a dialog with [buttonTextResId] as the button 2 label is displayed.
     */
    fun dialogWithButton2Displayed(@StringRes buttonTextResId: Int) {
        Espresso.onView(ViewMatchers.withId(android.R.id.button2))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.withText(buttonTextResId)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * Check if a dialog with [buttonTextResId] as the button 3 label is displayed.
     */
    fun dialogWithButton3Displayed(@StringRes buttonTextResId: Int) {
        Espresso.onView(ViewMatchers.withId(android.R.id.button3))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.withText(buttonTextResId)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * Check if the recycler view associated with [recyclerViewId] has the size of [size].
     */
    fun recyclerViewHasSize(@IdRes recyclerViewId: Int, size: Int) {
        Espresso.onView(
            AllOf.allOf<View>(
                ViewMatchers.withId(recyclerViewId),
                ViewMatchers.isDisplayed()
            )
        )
            .check(recyclerViewHasSize(size))
    }

    /**
     * Check if the recycler view associated with [viewIds]
     * are displayed within the view associated with [recyclerViewId].
     */
    fun viewDisplayedInRecyclerView(@IdRes recyclerViewId: Int, @IdRes vararg viewIds: Int) {
        val recyclerView =
            requireNotNull(currentActivity()).findViewById(recyclerViewId) as RecyclerView
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val visibleCount = layoutManager.findLastVisibleItemPosition() + 1

        Assert.assertTrue(visibleCount > 0)
        (0 until visibleCount).forEach { index ->
            // scroll to the item to make sure it's visible
            Espresso.onView(
                AllOf.allOf(
                    ViewMatchers.withId(recyclerViewId),
                    ViewMatchers.isDisplayed()
                )
            )
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(index))

            viewIds.forEach { viewId ->
                Espresso.onView(
                    withRecyclerView(recyclerViewId)
                        .atPositionOnView(index, viewId)
                )
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            }
        }
    }

    /**
     * Check if the current [Toolbar] has a title of [title].
     */
    fun toolbarHasTitle(title: String) {
        Espresso.onView(ViewMatchers.isAssignableFrom(Toolbar::class.java))
            .check(ViewAssertions.matches(withToolbarTitle(Is.`is`(title))))
    }

    /**
     * Check if the current [Toolbar] has a title of a string associated with [titleTextResId].
     */
    fun toolbarHasTitle(@StringRes titleTextResId: Int) {
        Espresso.onView(ViewMatchers.isAssignableFrom(Toolbar::class.java))
            .check(
                ViewAssertions.matches(
                    withToolbarTitle(
                        Is.`is`(
                            getApplicationContext<Context>().getString(titleTextResId)
                        )
                    )
                )
            )
    }

    /**
     * Check if the current [Toolbar] has a subtitle of [subtitle].
     */
    fun toolbarHasSubtitle(subtitle: String) {
        Espresso.onView(ViewMatchers.isAssignableFrom(Toolbar::class.java))
            .check(ViewAssertions.matches(withToolbarSubtitle(Is.`is`(subtitle))))
    }

    /**
     * Check if the current [Toolbar]
     * has a subtitle of a string associated with [subtitleTextResId].
     */
    fun toolbarHasSubtitle(@StringRes subtitleTextResId: Int) {
        Espresso.onView(ViewMatchers.isAssignableFrom(Toolbar::class.java))
            .check(
                ViewAssertions.matches(
                    withToolbarSubtitle(
                        Is.`is`(
                            getApplicationContext<Context>().getString(subtitleTextResId)
                        )
                    )
                )
            )
    }

    /**
     * Check if the radio group associated with [radioGroupId]
     * has [buttonTexts] as the labels of the radio buttons.
     */
    fun scrollableViewHasText(@IdRes radioGroupId: Int, vararg buttonTexts: String) {
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
    fun checkableViewSelected(@IdRes radioGroupId: Int, buttonText: String) {
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
    fun radioGroupHasSelections(@IdRes radioGroupId: Int) {
        Espresso.onView(AllOf.allOf(ViewMatchers.isDisplayed(), ViewMatchers.withId(radioGroupId)))
            .check(radioGroupHasSelections())
    }

    /**
     * Check if the radio group associated with [radioGroupId] has NO selection.
     */
    fun radioGroupHasNoSelections(@IdRes radioGroupId: Int) {
        Espresso.onView(AllOf.allOf(ViewMatchers.isDisplayed(), ViewMatchers.withId(radioGroupId)))
            .check(ViewAssertions.matches(not<Any>(radioGroupHasSelections())))
    }

    /**
     * Check if the drawable associated with [resourceId] is displayed.
     */
    fun drawableDisplayed(@IdRes resourceId: Int) {
        Espresso.onView(withDrawable(resourceId))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * Check if the bottom navigation view associated with [bottomNavigationViewResId]
     * has [selectedItemResId] as the selected item.
     */
    fun bottomNavigationViewItemSelected(
        @IdRes bottomNavigationViewResId: Int,
        @IdRes selectedItemResId: Int
    ) {
        Espresso.onView(ViewMatchers.withId(bottomNavigationViewResId))
            .check(ViewAssertions.matches(hasSelectedNavigationItem(selectedItemResId)))
    }

    /**
     * Check if the drawer associated with [drawerId] is opened.
     */
    fun drawerOpened(@IdRes drawerId: Int) {
        Espresso.onView(ViewMatchers.withId(drawerId))
            .check(ViewAssertions.matches(DrawerMatchers.isOpen()))
    }

    /**
     * Check if the drawer associated with [drawerId] is closed.
     */
    fun drawerClosed(@IdRes drawerId: Int) {
        Espresso.onView(ViewMatchers.withId(drawerId))
            .check(ViewAssertions.matches(DrawerMatchers.isClosed()))
    }

    /**
     * Check if a snackbar with [text] as message is displayed.
     */
    fun snackBarDisplayed(text: String) {
        Espresso.onView(
            allOf(
                ViewMatchers.withId(com.google.android.material.R.id.snackbar_text),
                ViewMatchers.withText(text)
            )
        )
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * Check if the checkable view associated with [viewId] is checked.
     */
    fun viewChecked(@IdRes viewId: Int) {
        Espresso.onView(ViewMatchers.withId(viewId))
            .check(ViewAssertions.matches(ViewMatchers.isChecked()))
    }

    /**
     * Check if the checkable view associated with [viewId] is NOT checked.
     */
    fun viewNotChecked(@IdRes viewId: Int) {
        Espresso.onView(ViewMatchers.withId(viewId))
            .check(ViewAssertions.matches(ViewMatchers.isNotChecked()))
    }

    /**
     * Check if the spinner view associated with [spinnerId] is has [text] as text.
     */
    fun spinnerHasText(@IdRes spinnerId: Int, text: String) {
        Espresso.onView(ViewMatchers.withId(spinnerId))
            .check(ViewAssertions.matches(ViewMatchers.withSpinnerText(text)))
    }

    /**
     * Check if no activity is currently displayed
     */
    fun noActivityDisplayed() {
        activityNotDisplayed(currentActivity())
    }

    /**
     * Check if the activity of type [A] has been launched.
     */
    inline fun <reified A : Activity> activityLaunched() {
        Intents.intended(IntentMatchers.hasComponent(A::class.java.name))
    }

    /**
     * Check if the fragment of type [F] with [tag] is displayed.
     */
    inline fun <reified F : Fragment> fragmentDisplayed(tag: String) {
        val fragment =
            (currentActivity() as FragmentActivity).supportFragmentManager.findFragmentByTag(tag)
        Assert.assertTrue(fragment != null && fragment.isVisible && fragment is F)
    }

    /**
     * Check if the fragment of type [F]
     * with a navigation host associated with [navHostViewId] is displayed.
     */
    inline fun <reified F : Fragment> fragmentDisplayed(@IdRes navHostViewId: Int) {
        val fragment = (currentActivity() as? FragmentActivity)?.supportFragmentManager
            ?.findFragmentById(navHostViewId)?.childFragmentManager?.primaryNavigationFragment

        Assert.assertTrue(fragment != null && fragment.isVisible && fragment is F)
    }
}
