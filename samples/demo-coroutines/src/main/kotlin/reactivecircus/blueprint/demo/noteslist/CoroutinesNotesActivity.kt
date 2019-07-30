package reactivecircus.blueprint.demo.noteslist

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import reactivecircus.blueprint.common.R
import reactivecircus.blueprint.demo.BlueprintCoroutinesDemoApp
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.enternote.CoroutinesEnterNoteActivity
import reactivecircus.blueprint.demo.enternote.EXTRA_ENTER_NOTE_PARAMS
import reactivecircus.blueprint.demo.enternote.EnterNoteParams
import reactivecircus.blueprint.demo.util.viewModel
import reactivecircus.blueprint.ui.extension.launchActivity

@FlowPreview
@ExperimentalCoroutinesApi
class CoroutinesNotesActivity : AppCompatActivity() {

    private val toolbar: Toolbar by lazy {
        findViewById(R.id.toolbar)
    }

    private val notesRecyclerView: RecyclerView by lazy {
        findViewById(R.id.recycler_view_notes)
    }

    private val progressBar: ProgressBar by lazy {
        findViewById(R.id.progress_bar)
    }

    private val noNotesTextView: TextView by lazy {
        findViewById(R.id.text_view_no_notes)
    }

    private val createNoteButton: ExtendedFloatingActionButton by lazy {
        findViewById(R.id.fab_add_note)
    }

    private lateinit var notesListAdapter: NotesListAdapter

    private val viewModel: CoroutinesNotesViewModel by viewModel {
        (application as BlueprintCoroutinesDemoApp).injector.provideNotesViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_notes)

        setSupportActionBar(toolbar)

        notesListAdapter = NotesListAdapter(itemClickedCallback)

        notesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CoroutinesNotesActivity)
            adapter = notesListAdapter
        }

        createNoteButton.setOnClickListener {
            launchActivity<CoroutinesEnterNoteActivity> {
                putExtra(EXTRA_ENTER_NOTE_PARAMS, EnterNoteParams.CreateNew)
            }
        }

        viewModel.notesLiveData.observe(this) { state ->
            when (state) {
                is State.LoadingNotes -> {
                    notesRecyclerView.isVisible = false
                    progressBar.isVisible = true
                    noNotesTextView.isVisible = false
                }
                is State.Idle -> {
                    state.run {
                        if (notes.isNotEmpty()) {
                            notesRecyclerView.isVisible = true
                            noNotesTextView.isVisible = false
                            notesListAdapter.submitList(notes)
                        } else {
                            notesRecyclerView.isVisible = false
                            noNotesTextView.isVisible = true
                        }
                    }
                    progressBar.isVisible = false
                }
            }
        }
    }

    private val itemClickedCallback: (note: Note) -> Unit = { note ->
        launchActivity<CoroutinesEnterNoteActivity> {
            putExtra(EXTRA_ENTER_NOTE_PARAMS, EnterNoteParams.Update(note.uuid))
        }
    }
}
