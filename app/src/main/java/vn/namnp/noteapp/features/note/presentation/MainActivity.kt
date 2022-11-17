package vn.namnp.noteapp.features.note.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import vn.namnp.noteapp.features.note.presentation.notes.NoteScreen
import vn.namnp.noteapp.features.note.presentation.screens.Screens
import vn.namnp.noteapp.ui.theme.NoteAppTheme

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        setContent {
            NoteAppTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screens.NotesScreen.route
                    ) {
                        composable(
                            route = Screens.NotesScreen.route
                        ) {
                            NoteScreen(
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}