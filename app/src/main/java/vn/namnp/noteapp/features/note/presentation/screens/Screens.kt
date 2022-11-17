package vn.namnp.noteapp.features.note.presentation.screens

sealed class Screens(val route: String) {
    object NotesScreen: Screens("NotesScreen")
    object AddEditNoteScreen: Screens("AddEditNoteScreen")
}