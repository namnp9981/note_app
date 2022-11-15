package vn.namnp.noteapp.features.note.domain.use_case

import vn.namnp.noteapp.features.note.domain.model.Note
import vn.namnp.noteapp.features.note.domain.repository.NoteRepository
import vn.namnp.noteapp.features.note.domain.util.InvalidNoteException
import kotlin.jvm.Throws

class AddNote(
    private val repository: NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if(note.title.isEmpty()) {
            throw InvalidNoteException("The title of the note can't be empty")
        }
        if(note.content.isEmpty()) {
            throw InvalidNoteException("The content of the note can't be empty")
        }
        repository.addNote(note)
    }
}