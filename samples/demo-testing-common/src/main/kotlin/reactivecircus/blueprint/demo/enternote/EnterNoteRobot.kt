package reactivecircus.blueprint.demo.enternote

import reactivecircus.blueprint.common.R
import reactivecircus.blueprint.testing.RobotActions
import reactivecircus.blueprint.testing.RobotAssertions
import reactivecircus.blueprint.testing.ScreenRobot
import reactivecircus.blueprint.testing.action.clickView
import reactivecircus.blueprint.testing.action.replaceTextInView
import reactivecircus.blueprint.testing.assertion.toolbarHasTitle
import reactivecircus.blueprint.testing.assertion.viewHasText

fun enterNoteScreen(block: EnterNoteRobot.() -> Unit) =
    EnterNoteRobot().apply { block() }

class EnterNoteRobot :
    ScreenRobot<EnterNoteRobotActions, EnterNoteRobotAssertions>(
        EnterNoteRobotActions(), EnterNoteRobotAssertions()
    )

class EnterNoteRobotActions : RobotActions {

    fun enterNote(note: String) {
        replaceTextInView(R.id.edit_text_note, note)
    }

    fun clickSaveButton() {
        clickView(R.id.action_save)
    }
}

class EnterNoteRobotAssertions : RobotAssertions {

    fun createNoteScreenTitleDisplayed() {
        toolbarHasTitle(R.string.title_create_note)
    }

    fun updateNoteScreenTitleDisplayed() {
        toolbarHasTitle(R.string.title_update_note)
    }

    fun noteDisplayed(note: String) {
        viewHasText(R.id.edit_text_note, note)
    }
}
