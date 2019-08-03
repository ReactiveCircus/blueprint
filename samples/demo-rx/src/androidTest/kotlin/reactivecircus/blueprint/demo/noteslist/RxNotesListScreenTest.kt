package reactivecircus.blueprint.demo.noteslist

import androidx.test.filters.LargeTest
import org.junit.Test
import reactivecircus.blueprint.demo.RxBaseScreenTest
import reactivecircus.blueprint.demo.enternote.RxEnterNoteActivity
import reactivecircus.blueprint.demo.testNotes
import reactivecircus.blueprint.testing.assertion.activityLaunched

@LargeTest
class RxNotesListScreenTest : RxBaseScreenTest() {

    override fun tearDown() {
        super.tearDown()
        noteCache.deleteAllNotes()
    }

    @Test
    fun openNotesListScreenWithExistingNotes_notesDisplayed() {
        notesListScreen {
            given {
                noteCache.addNotes(testNotes)
            }
            perform {
                launchActivityScenario<RxNotesListActivity>()
            }
            check {
                createNoteButtonDisplayed()
                notesDisplayed(testNotes)
            }
        }
    }

    @Test
    fun openNotesListScreenWithoutExistingNotes_emptyStateDisplayed() {
        notesListScreen {
            given {
                noteCache.deleteAllNotes()
            }
            perform {
                launchActivityScenario<RxNotesListActivity>()
            }
            check {
                createNoteButtonDisplayed()
                emptyStateDisplayed()
            }
        }
    }

    @Test
    fun clickNote_enterNoteScreenLaunched() {
        notesListScreen {
            given {
                noteCache.addNotes(testNotes)
            }
            perform {
                launchActivityScenario<RxNotesListActivity>()
                // select the first note
                clickNoteAt(0)
            }
            check {
                activityLaunched<RxEnterNoteActivity>()
            }
        }
    }

    @Test
    fun clickCreateNoteButton_enterNoteScreenLaunched() {
        notesListScreen {
            perform {
                launchActivityScenario<RxNotesListActivity>()
                clickCreateNoteButton()
            }
            check {
                activityLaunched<RxEnterNoteActivity>()
            }
        }
    }
}
