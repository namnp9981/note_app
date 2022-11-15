package vn.namnp.noteapp.features.note.domain.use_case

import vn.namnp.noteapp.features.note.domain.model.Note
import vn.namnp.noteapp.features.note.domain.repository.NoteRepository

class DeleteNote(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        noteRepository.deleteNote(note)
    }
}