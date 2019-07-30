package reactivecircus.blueprint.demo.noteslist

import androidx.test.filters.LargeTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.Test
import reactivecircus.blueprint.demo.CoroutinesBaseScreenTest
import reactivecircus.blueprint.demo.enternote.CoroutinesEnterNoteActivity
import reactivecircus.blueprint.demo.testNotes

@FlowPreview
@ExperimentalCoroutinesApi
@LargeTest
class CoroutinesNotesListScreenTest : CoroutinesBaseScreenTest() {

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
                launchActivityScenario<CoroutinesNotesActivity>()
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
                launchActivityScenario<CoroutinesNotesActivity>()
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
                launchActivityScenario<CoroutinesNotesActivity>()
                // select the first note
                clickNoteAt(0)
            }
            check {
                activityLaunched<CoroutinesEnterNoteActivity>()
            }
        }
    }

    @Test
    fun clickCreateNoteButton_enterNoteScreenLaunched() {
        notesListScreen {
            perform {
                launchActivityScenario<CoroutinesNotesActivity>()
                clickCreateNoteButton()
            }
            check {
                activityLaunched<CoroutinesEnterNoteActivity>()
            }
        }
    }
}
