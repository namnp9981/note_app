package vn.namnp.noteapp.features.note.presentation.add_edit_note

import androidx.compose.ui.focus.FocusState

sealed class AddEditNoteEvent {
    data class OnTitleChangeEvent(val value: String): AddEditNoteEvent()
    data class OnChangeFocusTitleEvent(val focusState: FocusState): AddEditNoteEvent()
    data class OnContentChangeEvent(val value: String): AddEditNoteEvent()
    data class OnChangeFocusContentEvent(val focusState: FocusState): AddEditNoteEvent()
    data class OnChangeColorEvent(val color: Int): AddEditNoteEvent()
    object OnSaveNoteEvent: AddEditNoteEvent()
}