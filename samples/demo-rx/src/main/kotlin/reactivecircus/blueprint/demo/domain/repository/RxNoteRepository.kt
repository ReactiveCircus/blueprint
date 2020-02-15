package reactivecircus.blueprint.demo.domain.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import reactivecircus.blueprint.demo.domain.model.Note

interface RxNoteRepository {

    fun streamAllNotes(): Observable<List<Note>>

    fun getNoteByUuid(uuid: String): Maybe<Note>

    fun addNote(note: Note): Completable

    fun updateNote(note: Note): Completable
}
