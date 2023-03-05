package vn.namnp.noteapp

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import vn.namnp.noteapp.features.note.domain.model.Note
import vn.namnp.noteapp.features.note.domain.use_case.GetNotes
import vn.namnp.noteapp.features.note.domain.util.NoteOrder
import vn.namnp.noteapp.features.note.domain.util.OrderType

class GetNotesUseCaseTest {
    private lateinit var getNotesUsecase: GetNotes
    private lateinit var fakeNoteRepository: FakeNoteRepository

    @Before
    fun setUp() {
        fakeNoteRepository = FakeNoteRepository()
        getNotesUsecase = GetNotes(fakeNoteRepository)

        val notesToAdd = mutableListOf<Note>()
        ('A'..'H').forEachIndexed { index, c ->
            notesToAdd.add(
                Note(
                    title = c.toString(),
                    content = c.toString(),
                    timestamp = index.toLong(),
                    color = index
                )
            )
        }
        notesToAdd.shuffle()
        runBlocking {
            notesToAdd.forEach { note ->
                fakeNoteRepository.addNote(note)
            }
        }
    }

    @Test
    fun `Order notes by Title-Ascending, Success`() = runBlocking {
        val notes = getNotesUsecase(NoteOrder.Title(OrderType.Ascending)).first()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].title).isLessThan(notes[i+1].title)
        }
    }

    @Test
    fun `Order notes by Title-Descending, Success`() = runBlocking {
        val notes = getNotesUsecase(NoteOrder.Title(OrderType.Descending)).first()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].title).isGreaterThan(notes[i+1].title)
        }
    }

    @Test
    fun `Order notes by Date-Ascending, Success`() = runBlocking {
        val notes = getNotesUsecase(NoteOrder.Date(OrderType.Ascending)).first()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].timestamp).isLessThan(notes[i+1].timestamp)
        }
    }

    @Test
    fun `Order notes by Date-Descending, Success`() = runBlocking {
        val notes = getNotesUsecase(NoteOrder.Date(OrderType.Descending)).first()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].timestamp).isGreaterThan(notes[i+1].timestamp)
        }
    }

    @Test
    fun `Order notes by Color-Ascending, Success`() = runBlocking {
        val notes = getNotesUsecase(NoteOrder.Color(OrderType.Ascending)).first()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].color).isLessThan(notes[i+1].color)
        }
    }

    @Test
    fun `Order notes by Color-Descending, Success`() = runBlocking {
        val notes = getNotesUsecase(NoteOrder.Color(OrderType.Descending)).first()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].color).isGreaterThan(notes[i+1].color)
        }
    }
}