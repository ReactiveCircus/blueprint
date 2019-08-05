# Blueprint Demo Testing Infrastructure

This library module has common UI testing infra code shared by the 2 apps, including the **screen robots** and **test data**.

A **Screen Robot** implementation uses the predefined view actions, view assertions and Robot DSL from the [blueprint-testing-robot][testing-robot] artifact to provide a layer of abstraction on top of the more general and primitive instrumentation commands.  

An example of a Robot implementation:

```kotlin
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
```

If the built-in robot actions and robot assertions are not sufficient, you can roll your custom actions or assertions directly using [Espresso][espresso]. Have a look at the custom **Robot Action** example in [blueprint-testing-robot][custom-robot-action].

[testing-robot]: /blueprint-testing-robot
[custom-robot-action]: /blueprint-testing-robot#building-custom-robot-actions-and-robot-assertions
[espresso]: https://developer.android.com/training/testing/espresso
