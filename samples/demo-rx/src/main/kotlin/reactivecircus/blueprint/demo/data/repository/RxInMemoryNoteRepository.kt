package reactivecircus.blueprint.demo.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.processors.BehaviorProcessor
import reactivecircus.blueprint.demo.data.cache.NoteCache
import reactivecircus.blueprint.demo.domain.model.Note
import reactivecircus.blueprint.demo.domain.repository.RxNoteRepository

class RxInMemoryNoteRepository(
    private val noteCache: NoteCache
) : RxNoteRepository {

    private val notesProcessor = BehaviorProcessor.createDefault(Unit).toSerialized()

    override fun streamAllNotes(): Observable<List<Note>> {
        return notesProcessor.map { noteCache.allNotes() }
            .toObservable()
    }

    override fun getNoteByUuid(uuid: String): Maybe<Note> {
        return Maybe.defer {
            val note = noteCache.findNote { it.uuid == uuid }
            if (note != null) {
                Maybe.just(note)
            } else {
                Maybe.empty()
            }
        }
    }

    override fun addNote(note: Note): Completable {
        return Completable.fromAction {
            noteCache.addNotes(listOf(note))
        }.doOnComplete {
            // refresh all notes stream
            notesProcessor.onNext(Unit)
        }
    }

    override fun updateNote(note: Note): Completable {
        return Completable.fromAction {
            noteCache.updateNote(note)
        }.doOnComplete {
            // refresh all notes stream
            notesProcessor.onNext(Unit)
        }
    }
}
