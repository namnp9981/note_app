package vn.namnp.noteapp.features.note.presentation.notes

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import vn.namnp.noteapp.common.testing.ElementTestIdentifier
import vn.namnp.noteapp.features.note.presentation.notes.components.NoteItem
import vn.namnp.noteapp.features.note.presentation.notes.components.OrderSection
import vn.namnp.noteapp.features.note.presentation.screens.Screens

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NoteScreen(
    viewModel: NotesViewModel = hiltViewModel(),
    navController: NavController
) {

    val state = viewModel.state.value
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screens.AddEditNoteScreen.route)
                },
                backgroundColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add a note"
                )
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "My notes", style = MaterialTheme.typography.h4)
                IconButton(
                    onClick = {
                        viewModel.onEvent(NoteEvent.ToggleOrderSectionEvent)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = "Sort notes"
                    )
                }
            }
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .testTag(ElementTestIdentifier.ORDER_SECTION)
                    ,
                    noteOrder = state.noteOrder,
                    onOrderChange = { newNoteOrder ->
                        viewModel.onEvent(NoteEvent.OrderNoteEvent(newNoteOrder))
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.notes) { note ->
                    NoteItem(
                        note = note,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    Screens.AddEditNoteScreen.route
                                            + "?noteId=${note.id}&noteColor=${note.color}"
                                )
                            },
                        onDeleteNoteClick = {
                            viewModel.onEvent(NoteEvent.DeleteNoteEvent(note))
                            scope.launch {
                                val result = scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Note deleted",
                                    actionLabel = "UNDO"
                                )
                                if(result == SnackbarResult.ActionPerformed) {
                                    viewModel.onEvent(NoteEvent.UndoDeletingNoteEvent)
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}