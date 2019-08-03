package reactivecircus.blueprint.demo

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import reactivecircus.blueprint.demo.data.cache.InMemoryNoteCache
import reactivecircus.blueprint.demo.data.cache.NoteCache
import reactivecircus.blueprint.demo.data.repository.RxInMemoryNoteRepository
import reactivecircus.blueprint.demo.domain.interactor.RxCreateNote
import reactivecircus.blueprint.demo.domain.interactor.RxGetNoteByUuid
import reactivecircus.blueprint.demo.domain.interactor.RxStreamAllNotes
import reactivecircus.blueprint.demo.domain.interactor.RxUpdateNote
import reactivecircus.blueprint.demo.domain.repository.RxNoteRepository
import reactivecircus.blueprint.demo.enternote.RxEnterNoteViewModel
import reactivecircus.blueprint.demo.noteslist.RxNotesListViewModel
import reactivecircus.blueprint.threading.rx2.SchedulerProvider

open class RxAppInjector {

    private val schedulerProvider: SchedulerProvider by lazy {
        SchedulerProvider(
            io = Schedulers.io(),
            computation = Schedulers.computation(),
            ui = AndroidSchedulers.mainThread()
        )
    }

    private val noteCache: NoteCache by lazy {
        InMemoryNoteCache()
    }

    private val noteRepository: RxNoteRepository by lazy {
        RxInMemoryNoteRepository(noteCache)
    }

    private val streamAllNotes: RxStreamAllNotes by lazy {
        RxStreamAllNotes(
            noteRepository = noteRepository,
            schedulerProvider = schedulerProvider
        )
    }

    private val getNoteByUuid: RxGetNoteByUuid by lazy {
        RxGetNoteByUuid(
            noteRepository = noteRepository,
            schedulerProvider = schedulerProvider
        )
    }

    private val createNote: RxCreateNote by lazy {
        RxCreateNote(
            noteRepository = noteRepository,
            schedulerProvider = schedulerProvider
        )
    }

    private val updateNote: RxUpdateNote by lazy {
        RxUpdateNote(
            noteRepository = noteRepository,
            schedulerProvider = schedulerProvider
        )
    }

    open fun provideSchedulerProvider(): SchedulerProvider {
        return schedulerProvider
    }

    open fun provideNoteCache(): NoteCache {
        return noteCache
    }

    fun provideNotesListViewModel(): RxNotesListViewModel {
        return RxNotesListViewModel(streamAllNotes)
    }

    fun provideEnterNoteViewModel(noteUuid: String?): RxEnterNoteViewModel {
        return RxEnterNoteViewModel(
            noteUuid,
            getNoteByUuid,
            createNote,
            updateNote
        )
    }
}
