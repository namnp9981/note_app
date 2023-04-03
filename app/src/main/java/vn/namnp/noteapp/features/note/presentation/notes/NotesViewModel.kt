package vn.namnp.noteapp.features.note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import vn.namnp.noteapp.features.note.domain.model.Note
import vn.namnp.noteapp.features.note.domain.use_case.NoteUseCases
import vn.namnp.noteapp.features.note.domain.util.NoteOrder
import vn.namnp.noteapp.features.note.domain.util.OrderType
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var recentlyDeletedNote: Note? = null

    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(noteEvent: NoteEvent) {
        when (noteEvent) {
            is NoteEvent.ToggleOrderSectionEvent -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
            is NoteEvent.UndoDeletingNoteEvent -> {
                viewModelScope.launch {
                    noteUseCases.addNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }
            is NoteEvent.DeleteNoteEvent -> {
                viewModelScope.launch {
                    noteUseCases.deleteNote(noteEvent.note)
                    recentlyDeletedNote = noteEvent.note
                }
            }
            is NoteEvent.OrderNoteEvent -> {
                if (state.value.noteOrder::class == noteEvent.noteOrder::class
                    && state.value.noteOrder == noteEvent.noteOrder
                ) {
                    return
                }
                getNotes(noteEvent.noteOrder)
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()

//        getNotesJob = viewModelScope.launch {
//            noteUseCases.getNotes(noteOrder)
//                .collectLatest { notes ->
//                    _state.value = state.value.copy(
//                        notes = notes,
//                        noteOrder = noteOrder
//                    )
//                }
//        }
        getNotesJob = noteUseCases.getNotes(noteOrder)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }
}