package reactivecircus.blueprint.demo.enternote

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.LargeTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import reactivecircus.blueprint.demo.CoroutinesBaseScreenTest
import reactivecircus.blueprint.demo.noteslist.CoroutinesNotesListActivity
import reactivecircus.blueprint.demo.noteslist.notesListScreen
import reactivecircus.blueprint.demo.testNotes
import reactivecircus.blueprint.testing.action.clickNavigateUpButton
import reactivecircus.blueprint.ui.extension.newIntent

@ExperimentalCoroutinesApi
@LargeTest
class CoroutinesEnterNoteScreenTest : CoroutinesBaseScreenTest() {

    override fun tearDown() {
        super.tearDown()
        noteCache.deleteAllNotes()
    }

    @Test
    fun openEnterNoteScreenInCreateMode_emptyNoteDisplayed() {
        enterNoteScreen {
            perform {
                launchActivityScenario<CoroutinesEnterNoteActivity>(
                    enterNoteLaunchIntent(EnterNoteParams.CreateNew)
                )
            }
            check {
                createNoteScreenTitleDisplayed()
                noteDisplayed("")
            }
        }
    }

    @Test
    fun openEnterNoteScreenInUpdateMode_emptyNoteDisplayed() {
        enterNoteScreen {
            given {
                noteCache.addNotes(listOf(testNotes[0]))
            }
            perform {
                launchActivityScenario<CoroutinesEnterNoteActivity>(
                    enterNoteLaunchIntent(EnterNoteParams.Update(uuid = testNotes[0].uuid))
                )
            }
            check {
                updateNoteScreenTitleDisplayed()
                noteDisplayed(testNotes[0].content)
            }
        }
    }

    @Test
    fun clickSaveButtonInCreateMode_newNoteCreated() {
        notesListScreen {
            given {
                noteCache.deleteAllNotes()
            }
            perform {
                launchActivityScenario<CoroutinesNotesListActivity>()
                clickCreateNoteButton()
            }
        }

        enterNoteScreen {
            perform {
                enterNote("New note")
                clickSaveButton()
            }
        }

        notesListScreen {
            check {
                noteContentDisplayedAt(0, "New note")
            }
        }
    }

    @Test
    fun clickCloseButtonInCreateMode_noNewNoteCreated() {
        notesListScreen {
            given {
                noteCache.deleteAllNotes()
            }
            perform {
                launchActivityScenario<CoroutinesNotesListActivity>()
                clickCreateNoteButton()
            }
        }

        enterNoteScreen {
            perform {
                enterNote("New note")
                clickNavigateUpButton()
            }
        }

        notesListScreen {
            check {
                emptyStateDisplayed()
            }
        }
    }

    @Test
    fun clickSaveButtonInUpdateMode_noteUpdated() {
        notesListScreen {
            given {
                noteCache.addNotes(testNotes)
            }
            perform {
                launchActivityScenario<CoroutinesNotesListActivity>()
                clickNoteAt(0)
            }
        }

        enterNoteScreen {
            perform {
                enterNote("Updated note")
                clickSaveButton()
            }
        }

        notesListScreen {
            check {
                noteContentDisplayedAt(0, "Updated note")
            }
        }
    }

    @Test
    fun clickCloseButtonInUpdateMode_noteNotCreated() {
        notesListScreen {
            given {
                noteCache.addNotes(listOf(testNotes[0]))
            }
            perform {
                launchActivityScenario<CoroutinesNotesListActivity>()
                clickNoteAt(0)
            }
        }

        enterNoteScreen {
            perform {
                enterNote("Updated note")
                clickNavigateUpButton()
            }
        }

        notesListScreen {
            check {
                noteContentDisplayedAt(0, testNotes[0].content)
            }
        }
    }

    private fun enterNoteLaunchIntent(params: EnterNoteParams): Intent {
        return newIntent<CoroutinesEnterNoteActivity>(ApplicationProvider.getApplicationContext()).apply {
            putExtra(EXTRA_ENTER_NOTE_PARAMS, params)
        }
    }
}
