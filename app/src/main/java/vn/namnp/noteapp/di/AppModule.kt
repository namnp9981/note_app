package vn.namnp.noteapp.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.namnp.noteapp.features.note.data.data_source.NoteDatabase
import vn.namnp.noteapp.features.note.data.repository.NoteRepositoryImpl
import vn.namnp.noteapp.features.note.domain.repository.NoteRepository
import vn.namnp.noteapp.features.note.domain.use_case.*
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDB(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(noteDb: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(noteDb.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCase(noteRepository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(noteRepository),
            getNote = GetNote(noteRepository),
            addNote = AddNote(noteRepository),
            deleteNote = DeleteNote(noteRepository)
        )
    }

}