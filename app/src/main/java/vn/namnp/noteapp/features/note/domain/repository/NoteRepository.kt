package vn.namnp.noteapp.features.note.domain.repository

import kotlinx.coroutines.flow.Flow
import vn.namnp.noteapp.features.note.domain.model.Note

interface NoteRepository {

    fun getNotes() : Flow<List<Note>>

    suspend fun getNoteById(id: Int) : Note?

    suspend fun addNote(note: Note)

    suspend fun deleteNote(note: Note)
}