package vn.namnp.noteapp.end_to_end_test

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import vn.namnp.noteapp.common.testing.ElementTestIdentifier
import vn.namnp.noteapp.di.AppModule
import vn.namnp.noteapp.features.note.presentation.MainActivity
import vn.namnp.noteapp.features.note.presentation.add_edit_note.AddEditNoteScreen
import vn.namnp.noteapp.features.note.presentation.notes.NoteScreen
import vn.namnp.noteapp.features.note.presentation.screens.Screens
import vn.namnp.noteapp.ui.theme.NoteAppTheme

@HiltAndroidTest
@UninstallModules(AppModule::class)
class NoteAppEndToEndTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @ExperimentalAnimationApi
    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            NoteAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screens.NotesScreen.route
                ) {
                    composable(route = Screens.NotesScreen.route) {
                        NoteScreen(navController = navController)
                    }
                    composable(
                        route = Screens.AddEditNoteScreen.route +
                                "?noteId={noteId}&noteColor={noteColor}",
                        arguments = listOf(
                            navArgument(
                                name = "noteId"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            },
                            navArgument(
                                name = "noteColor"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            },
                        )
                    ) {
                        val color = it.arguments?.getInt("noteColor") ?: -1
                        AddEditNoteScreen(
                            navController = navController,
                            noteColor = color
                        )
                    }
                }
            }
        }
    }

    @Test
    fun saveNewNote_success() {
        // Click on FAB to get to add note screen
        composeRule.onNodeWithContentDescription(ElementTestIdentifier.ADD_NOTE).performClick()

        val titleSampleInput = "test-title"
        val contentSampleInput = "content-title"

        // Enter texts in title and content text fields
        composeRule
            .onNodeWithTag(ElementTestIdentifier.TITLE_TEXT_FIELD)
            .performTextInput(titleSampleInput)
        composeRule
            .onNodeWithTag(ElementTestIdentifier.CONTENT_TEXT_FIELD)
            .performTextInput(contentSampleInput)
        // Save note
        composeRule.onNodeWithContentDescription(ElementTestIdentifier.SAVE_NOTE).performClick()

        // check added note is displayed
        composeRule.onNodeWithText(titleSampleInput).assertIsDisplayed()
    }

    @Test
    fun saveNewNote_editAfterwards() {
        // Click on FAB to get to add note screen
        composeRule.onNodeWithContentDescription(ElementTestIdentifier.ADD_NOTE).performClick()

        val titleSampleInput = "test-title"
        val contentSampleInput = "content-title"

        // Enter texts in title and content text fields
        composeRule
            .onNodeWithTag(ElementTestIdentifier.TITLE_TEXT_FIELD)
            .performTextInput(titleSampleInput)
        composeRule
            .onNodeWithTag(ElementTestIdentifier.CONTENT_TEXT_FIELD)
            .performTextInput(contentSampleInput)
        // Save note
        composeRule.onNodeWithContentDescription(ElementTestIdentifier.SAVE_NOTE).performClick()

        // check added note is displayed
        composeRule.onNodeWithText(titleSampleInput).assertIsDisplayed()
        // Click on note to edit it
        composeRule.onNodeWithText(titleSampleInput).performClick()

        // check edit the right note
        composeRule
            .onNodeWithTag(ElementTestIdentifier.TITLE_TEXT_FIELD)
            .assertTextEquals(titleSampleInput)
        composeRule
            .onNodeWithTag(ElementTestIdentifier.CONTENT_TEXT_FIELD)
            .assertTextEquals(contentSampleInput)
        // append the text "update" to the title text field
        composeRule
            .onNodeWithTag(ElementTestIdentifier.TITLE_TEXT_FIELD)
            .performTextInput("-update")
        // Update the note
        composeRule.onNodeWithContentDescription(ElementTestIdentifier.SAVE_NOTE).performClick()

        // check the note is updated
        composeRule.onNodeWithText("$titleSampleInput-update").assertIsDisplayed()
    }

    @Test
    fun saveNewNotes_orderByTitleDescending() {
        for (i in 1..3) {
            // Click on FAB to get to add note screen
            composeRule.onNodeWithContentDescription(ElementTestIdentifier.ADD_NOTE).performClick()

            // Enter texts in title and content text fields
            composeRule
                .onNodeWithTag(ElementTestIdentifier.TITLE_TEXT_FIELD)
                .performTextInput(i.toString())
            composeRule
                .onNodeWithTag(ElementTestIdentifier.CONTENT_TEXT_FIELD)
                .performTextInput(i.toString())
            // Save the new
            composeRule.onNodeWithContentDescription(ElementTestIdentifier.SAVE_NOTE).performClick()
        }

        composeRule.onNodeWithText("1").assertIsDisplayed()
        composeRule.onNodeWithText("2").assertIsDisplayed()
        composeRule.onNodeWithText("3").assertIsDisplayed()

        composeRule
            .onNodeWithContentDescription(ElementTestIdentifier.SORT_NOTE)
            .performClick()
        composeRule
            .onNodeWithContentDescription("Title")
            .performClick()
        composeRule
            .onNodeWithContentDescription("Descending")
            .performClick()

        composeRule.onAllNodesWithTag(ElementTestIdentifier.NOTE_ITEM)[0]
            .assertTextContains("3")
        composeRule.onAllNodesWithTag(ElementTestIdentifier.NOTE_ITEM)[1]
            .assertTextContains("2")
        composeRule.onAllNodesWithTag(ElementTestIdentifier.NOTE_ITEM)[2]
            .assertTextContains("1")
    }

}
