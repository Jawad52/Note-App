package com.jawad.noteapp.di

import android.app.Application
import androidx.room.Room
import com.jawad.noteapp.feature_note.data.local.NoteDao
import com.jawad.noteapp.feature_note.data.local.NoteDatabase
import com.jawad.noteapp.feature_note.data.repository.NoteRepositoryImpl
import com.jawad.noteapp.feature_note.domain.repository.NoteRepository
import com.jawad.noteapp.feature_note.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app, NoteDatabase::class.java,
            NoteDatabase.DATA_BASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providesNotesRepository(noteDao: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(noteDao.getNoteDao)
    }

    @Provides
    @Singleton
    fun providesUseCase(noteRepository: NoteRepository): NoteUseCase {
        return NoteUseCase(
            getNoteUseCase = GetNoteUseCase(noteRepository),
            deleteNoteUseCase = DeleteNoteUseCase(noteRepository),
            addNoteUseCase = AddNoteUseCase(noteRepository),
            getNoteByIdUseCase = GetNoteByIdUseCase(noteRepository)
        )
    }
}