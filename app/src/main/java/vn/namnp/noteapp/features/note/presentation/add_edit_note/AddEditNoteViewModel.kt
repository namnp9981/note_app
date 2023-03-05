package vn.namnp.noteapp.features.note.presentation.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import vn.namnp.noteapp.features.note.domain.model.Note
import vn.namnp.noteapp.features.note.domain.use_case.NoteUseCases
import vn.namnp.noteapp.features.note.domain.util.InvalidNoteException
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _noteTitle = mutableStateOf(NoteTextFieldState(
        hint = "Enter title..."
    ))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState(
        hint = "Enter some content..."
    ))
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _noteColor = mutableStateOf(Note.noteColors.random().toArgb())
    val noteColor = _noteColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if(noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = _noteTitle.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        _noteContent.value = _noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        _noteColor.value = note.color
                    }
                }
            }
        }
    }

    fun onEvent(noteEvent: AddEditNoteEvent) {
        when(noteEvent) {
            is AddEditNoteEvent.OnTitleChangeEvent -> {
                _noteTitle.value = _noteTitle.value.copy(
                    text = noteEvent.value
                )
            }
            is AddEditNoteEvent.OnContentChangeEvent -> {
                _noteContent.value = _noteContent.value.copy(
                    text = noteEvent.value
                )
            }
            is AddEditNoteEvent.OnChangeFocusTitleEvent -> {
                _noteTitle.value = _noteTitle.value.copy(
                    isHintVisible = !noteEvent.focusState.isFocused && _noteTitle.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.OnChangeFocusContentEvent -> {
                _noteContent.value = _noteContent.value.copy(
                    isHintVisible = !noteEvent.focusState.isFocused && _noteContent.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.OnChangeColorEvent -> {
                _noteColor.value = noteEvent.color
            }
            is AddEditNoteEvent.OnSaveNoteEvent -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                title = _noteTitle.value.text,
                                content = _noteContent.value.text,
                                color = _noteColor.value,
                                id = currentNoteId,
                                timestamp = System.currentTimeMillis(),
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    }catch (e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String): UiEvent()
        object SaveNote: UiEvent()
    }
}
