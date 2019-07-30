package reactivecircus.blueprint.demo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import reactivecircus.blueprint.demo.data.cache.InMemoryNoteCache
import reactivecircus.blueprint.demo.data.cache.NoteCache
import reactivecircus.blueprint.demo.data.repository.CoroutinesInMemoryNoteRepository
import reactivecircus.blueprint.demo.domain.interactor.CoroutinesCreateNote
import reactivecircus.blueprint.demo.domain.interactor.CoroutinesGetNoteByUuid
import reactivecircus.blueprint.demo.domain.interactor.CoroutinesStreamAllNotes
import reactivecircus.blueprint.demo.domain.interactor.CoroutinesUpdateNote
import reactivecircus.blueprint.demo.domain.repository.CoroutinesNoteRepository
import reactivecircus.blueprint.demo.enternote.CoroutinesEnterNoteViewModel
import reactivecircus.blueprint.demo.noteslist.CoroutinesNotesViewModel
import reactivecircus.blueprint.threading.coroutines.CoroutineDispatchers

@FlowPreview
@ExperimentalCoroutinesApi
open class CoroutinesAppInjector {

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

    private val noteRepository: CoroutinesNoteRepository by lazy {
        CoroutinesInMemoryNoteRepository(noteCache)
    }

    private val streamAllNotes: CoroutinesStreamAllNotes by lazy {
        CoroutinesStreamAllNotes(
            noteRepository = noteRepository,
            coroutineDispatchers = coroutineDispatchers
        )
    }

    private val getNoteByUuid: CoroutinesGetNoteByUuid by lazy {
        CoroutinesGetNoteByUuid(
            noteRepository = noteRepository,
            coroutineDispatchers = coroutineDispatchers
        )
    }

    private val createNote: CoroutinesCreateNote by lazy {
        CoroutinesCreateNote(
            noteRepository = noteRepository,
            coroutineDispatchers = coroutineDispatchers
        )
    }

    private val updateNote: CoroutinesUpdateNote by lazy {
        CoroutinesUpdateNote(
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

    fun provideNotesViewModel(): CoroutinesNotesViewModel {
        return CoroutinesNotesViewModel(streamAllNotes)
    }

    fun provideEnterNoteViewModel(noteUuid: String?): CoroutinesEnterNoteViewModel {
        return CoroutinesEnterNoteViewModel(
            noteUuid,
            getNoteByUuid,
            createNote,
            updateNote
        )
    }
}
