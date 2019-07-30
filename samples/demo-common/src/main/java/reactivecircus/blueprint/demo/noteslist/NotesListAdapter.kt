package reactivecircus.blueprint.demo.noteslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import reactivecircus.blueprint.common.R
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.util.toFormattedDateString

const val LAST_UPDATED_DATE_PATTERN = "dd MMM yyyy HH:mm"

class NotesListAdapter(
    private val itemClickedCallback: (item: Note) -> Unit
) : ListAdapter<Note, NoteViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position), itemClickedCallback)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_note
    }
}

class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val noteContentTextView: AppCompatTextView =
        itemView.findViewById(R.id.text_view_note_content)

    private val timeLastUpdatedTextView: AppCompatTextView =
        itemView.findViewById(R.id.text_view_time_last_updated)

    init {
        itemView.tag = this
    }

    fun bind(note: Note, itemClickedCallback: (item: Note) -> Unit) {
        itemView.run {
            noteContentTextView.setTextFuture(
                PrecomputedTextCompat.getTextFuture(
                    note.content,
                    TextViewCompat.getTextMetricsParams(noteContentTextView),
                    null
                )
            )

            val formattedTimeLastUpdated =
                note.timeLastUpdated.toFormattedDateString(LAST_UPDATED_DATE_PATTERN)
            timeLastUpdatedTextView.setTextFuture(
                PrecomputedTextCompat.getTextFuture(
                    formattedTimeLastUpdated,
                    TextViewCompat.getTextMetricsParams(timeLastUpdatedTextView),
                    null
                )
            )
        }

        itemView.setOnClickListener {
            itemClickedCallback(note)
        }
    }
}

private val diffCallback: DiffUtil.ItemCallback<Note> =
    object : DiffUtil.ItemCallback<Note>() {

        override fun areItemsTheSame(
            oldItem: Note,
            newItem: Note
        ) = oldItem.uuid == newItem.uuid

        override fun areContentsTheSame(
            oldItem: Note,
            newItem: Note
        ) = oldItem == newItem
    }
