package reactivecircus.blueprint.demo.domain.repository

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import reactivecircus.blueprint.demo.domain.model.Note

interface RxNoteRepository {

    fun streamAllNotes(): Observable<List<Note>>

    fun getNoteByUuid(uuid: String): Maybe<Note>

    fun addNote(note: Note): Completable

    fun updateNote(note: Note): Completable
}
