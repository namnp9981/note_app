package vn.namnp.noteapp.features.note.data.repository

import kotlinx.coroutines.flow.Flow
import vn.namnp.noteapp.features.note.data.data_source.NoteDao
import vn.namnp.noteapp.features.note.domain.model.Note
import vn.namnp.noteapp.features.note.domain.repository.NoteRepository

class NoteRepositoryImpl(
    private val dao: NoteDao
): NoteRepository {
    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes()
    }

    override suspend fun getNoteById(id: Int) : Note? {
        return dao.getNoteById(id)
    }

    override suspend fun addNote(note: Note) {
        dao.addNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note)
    }
}