package reactivecircus.blueprint.demo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import reactivecircus.blueprint.demo.data.cache.InMemoryNoteCache
import reactivecircus.blueprint.demo.data.cache.NoteCache
import reactivecircus.blueprint.demo.data.repository.InMemoryNoteRepository
import reactivecircus.blueprint.demo.domain.interactor.CreateNote
import reactivecircus.blueprint.demo.domain.interactor.GetNoteByUuid
import reactivecircus.blueprint.demo.domain.interactor.StreamAllNotes
import reactivecircus.blueprint.demo.domain.interactor.UpdateNote
import reactivecircus.blueprint.demo.domain.repository.NoteRepository
import reactivecircus.blueprint.demo.enternote.EnterNoteViewModel
import reactivecircus.blueprint.demo.noteslist.NotesViewModel
import reactivecircus.blueprint.threading.coroutines.CoroutineDispatchers

@FlowPreview
@ExperimentalCoroutinesApi
open class Injector {

    private val coroutineDispatchers: CoroutineDispatchers by lazy {
        CoroutineDispatchers(
            io = Dispatchers.IO,
            computation = Dispatchers.Default,
            ui = Dispatchers.Main
        )
    }

    private val noteCache: NoteCache by lazy {
        InMemoryNoteCache()
    }

    private val noteRepository: NoteRepository by lazy {
        InMemoryNoteRepository(noteCache)
    }

    private val streamAllNotes: StreamAllNotes by lazy {
        StreamAllNotes(
            noteRepository = noteRepository,
            coroutineDispatchers = coroutineDispatchers
        )
    }

    private val getNoteByUuid: GetNoteByUuid by lazy {
        GetNoteByUuid(
            noteRepository = noteRepository,
            coroutineDispatchers = coroutineDispatchers
        )
    }

    private val createNote: CreateNote by lazy {
        CreateNote(
            noteRepository = noteRepository,
            coroutineDispatchers = coroutineDispatchers
        )
    }

    private val updateNote: UpdateNote by lazy {
        UpdateNote(
            noteRepository = noteRepository,
            coroutineDispatchers = coroutineDispatchers
        )
    }

    open fun provideCoroutineDispatchers(): CoroutineDispatchers {
        return coroutineDispatchers
    }

    open fun provideNoteCache(): NoteCache {
        return noteCache
    }

    fun provideNotesViewModel(): NotesViewModel {
        return NotesViewModel(streamAllNotes)
    }

    fun provideEnterNoteViewModel(noteUuid: String?): EnterNoteViewModel {
        return EnterNoteViewModel(
            noteUuid,
            getNoteByUuid,
            createNote,
            updateNote
        )
    }
}
