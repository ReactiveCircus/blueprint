package reactivecircus.blueprint.demo.enternote

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import reactivecircus.blueprint.common.R
import reactivecircus.blueprint.demo.BlueprintCoroutinesDemoApp
import reactivecircus.blueprint.demo.util.viewModel

const val EXTRA_ENTER_NOTE_PARAMS = "EXTRA_ENTER_NOTE_PARAMS"

@FlowPreview
@ExperimentalCoroutinesApi
class CoroutinesEnterNoteActivity : AppCompatActivity() {

    private val params: EnterNoteParams? by lazy {
        intent.getParcelableExtra(EXTRA_ENTER_NOTE_PARAMS)
    }

    private val toolbar: Toolbar by lazy {
        findViewById(R.id.toolbar)
    }

    private val noteEditText: EditText by lazy {
        findViewById(R.id.edit_text_note)
    }

    private val viewModel: CoroutinesEnterNoteViewModel by viewModel {
        val noteUuid = (params as? EnterNoteParams.Update)?.uuid
        (application as BlueprintCoroutinesDemoApp).injector.provideEnterNoteViewModel(noteUuid)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_enter_note)

        toolbar.title = if (params is EnterNoteParams.CreateNew) {
            getString(R.string.title_create_note)
        } else {
            getString(R.string.title_update_note)
        }

        setSupportActionBar(toolbar)

        toolbar.apply {
            setNavigationIcon(R.drawable.ic_close_primary_24dp)
            setNavigationOnClickListener { finish() }
        }

        viewModel.noteLiveData.observe(this) { state ->
            state.note?.run {
                noteEditText.setText(this.content)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_enter_note, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_save) {
            if (noteEditText.text.toString().isNotBlank()) {
                when (params) {
                    is EnterNoteParams.CreateNew -> {
                        viewModel.createNote(noteEditText.text.toString().trim())
                    }
                    is EnterNoteParams.Update -> {
                        val updatedNote = viewModel.noteLiveData.value?.note!!.copy(
                            content = noteEditText.text.toString().trim(),
                            timeLastUpdated = System.currentTimeMillis()
                        )
                        viewModel.updateNote(updatedNote)
                    }
                }
            }

            finish()
            true
        }
        else super.onOptionsItemSelected(item)
    }
}
