package reactivecircus.blueprint.demo.noteslist

import androidx.test.filters.LargeTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.Test
import reactivecircus.blueprint.demo.BaseScreenTest
import reactivecircus.blueprint.demo.enternote.EnterNoteActivity
import reactivecircus.blueprint.demo.testNotes

@FlowPreview
@ExperimentalCoroutinesApi
@LargeTest
class NotesListScreenTest : BaseScreenTest() {

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
                launchActivityScenario<NotesActivity>()
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
                launchActivityScenario<NotesActivity>()
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
                launchActivityScenario<NotesActivity>()
                // select the first note
                clickNoteAt(0)
            }
            check {
                activityLaunched<EnterNoteActivity>()
            }
        }
    }

    @Test
    fun clickCreateNoteButton_enterNoteScreenLaunched() {
        notesListScreen {
            perform {
                launchActivityScenario<NotesActivity>()
                clickCreateNoteButton()
            }
            check {
                activityLaunched<EnterNoteActivity>()
            }
        }
    }
}
