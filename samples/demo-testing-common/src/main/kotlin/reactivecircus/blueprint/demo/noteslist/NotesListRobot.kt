package reactivecircus.blueprint.demo.noteslist

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import reactivecircus.blueprint.common.R
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.util.toFormattedDateString
import reactivecircus.blueprint.testing.RobotActions
import reactivecircus.blueprint.testing.RobotAssertions
import reactivecircus.blueprint.testing.ScreenRobot
import reactivecircus.blueprint.testing.action.clickRecyclerViewItem
import reactivecircus.blueprint.testing.action.clickView
import reactivecircus.blueprint.testing.assertion.recyclerViewHasSize
import reactivecircus.blueprint.testing.assertion.textDisplayed
import reactivecircus.blueprint.testing.assertion.viewDisplayed
import reactivecircus.blueprint.testing.assertion.viewNotDisplayed
import reactivecircus.blueprint.testing.scrollToItemInRecyclerView
import reactivecircus.blueprint.testing.matcher.withRecyclerView

fun notesListScreen(block: NotesListRobot.() -> Unit) =
    NotesListRobot().apply { block() }

class NotesListRobot :
    ScreenRobot<NotesListRobotActions, NotesListRobotAssertions>(
        NotesListRobotActions(),
        NotesListRobotAssertions()
    )

class NotesListRobotActions : RobotActions {

    fun clickNoteAt(position: Int) {
        clickRecyclerViewItem(R.id.recycler_view_notes, position)
    }

    fun clickCreateNoteButton() {
        clickView(R.id.fab_add_note)
    }
}

class NotesListRobotAssertions : RobotAssertions {

    fun emptyStateDisplayed() {
        viewDisplayed(R.id.text_view_no_notes)
        textDisplayed(R.string.no_notes)
        viewNotDisplayed(R.id.recycler_view_notes)
    }

    fun notesDisplayed(notes: List<Note>) {
        viewNotDisplayed(R.id.text_view_no_notes)
        viewDisplayed(R.id.recycler_view_notes)

        val recyclerViewId = R.id.recycler_view_notes
        val noteContentTextViewId = R.id.text_view_note_content
        val timeLastUpdatedTextViewId = R.id.text_view_time_last_updated

        recyclerViewHasSize(recyclerViewId, notes.size)

        notes.forEachIndexed { index, note ->
            // scroll to the item to make sure it's visible
            scrollToItemInRecyclerView(recyclerViewId, index)

            onView(
                withRecyclerView(recyclerViewId)
                    .atPositionOnView(index, noteContentTextViewId)
            )
                .check(matches(withText(note.content)))

            onView(
                withRecyclerView(recyclerViewId)
                    .atPositionOnView(index, timeLastUpdatedTextViewId)
            )
                .check(
                    matches(
                        withText(
                            note.timeLastUpdated.toFormattedDateString(
                                LAST_UPDATED_DATE_PATTERN
                            )
                        )
                    )
                )
        }
    }

    fun noteContentDisplayedAt(position: Int, note: String) {
        viewNotDisplayed(R.id.text_view_no_notes)
        viewDisplayed(R.id.recycler_view_notes)

        val recyclerViewId = R.id.recycler_view_notes
        val noteContentTextViewId = R.id.text_view_note_content

        // scroll to the item to make sure it's visible
        scrollToItemInRecyclerView(recyclerViewId, position)

        onView(
            withRecyclerView(recyclerViewId)
                .atPositionOnView(position, noteContentTextViewId)
        )
            .check(matches(withText(note)))
    }

    fun createNoteButtonDisplayed() {
        viewDisplayed(R.id.fab_add_note)
    }
}
