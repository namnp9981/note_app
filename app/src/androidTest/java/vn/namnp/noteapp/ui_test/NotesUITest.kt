package vn.namnp.noteapp.ui_test

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import vn.namnp.noteapp.common.testing.ElementTestIdentifier
import vn.namnp.noteapp.di.AppModule
import vn.namnp.noteapp.features.note.presentation.MainActivity
import vn.namnp.noteapp.features.note.presentation.notes.NoteScreen
import vn.namnp.noteapp.features.note.presentation.screens.Screens
import vn.namnp.noteapp.ui.theme.NoteAppTheme

@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesUITest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @ExperimentalAnimationApi
    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            val navController = rememberNavController()
            NoteAppTheme {
                NavHost(
                    navController = navController,
                    startDestination = Screens.NotesScreen.route
                ) {
                    composable(route = Screens.NotesScreen.route) {
                        NoteScreen(navController = navController)
                    }
                }
            }
        }
    }

    @Test
    fun clickToggleOrderSection_isVisible() {
        composeRule.onNodeWithTag(ElementTestIdentifier.ORDER_SECTION).assertDoesNotExist()
        composeRule.onNodeWithContentDescription("Sort notes").performClick()
        composeRule.onNodeWithTag(ElementTestIdentifier.ORDER_SECTION).assertIsDisplayed()
    }
}