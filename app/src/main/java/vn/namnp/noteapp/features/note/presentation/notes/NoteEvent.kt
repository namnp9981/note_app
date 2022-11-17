package vn.namnp.noteapp.features.note.presentation.notes

import vn.namnp.noteapp.features.note.domain.model.Note
import vn.namnp.noteapp.features.note.domain.util.NoteOrder
import vn.namnp.noteapp.features.note.domain.util.OrderType

sealed class NoteEvent {
    data class OrderNoteEvent(val noteOrder: NoteOrder): NoteEvent()
    data class DeleteNoteEvent(val note: Note): NoteEvent()
    object UndoDeletingNoteEvent: NoteEvent()
    object ToggleOrderSectionEvent: NoteEvent()
}
