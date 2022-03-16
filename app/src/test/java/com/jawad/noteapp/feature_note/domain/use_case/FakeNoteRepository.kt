package com.jawad.noteapp.feature_note.domain.use_case

import com.jawad.noteapp.feature_note.domain.model.Note
import com.jawad.noteapp.feature_note.domain.repository.NoteRepository
import com.jawad.noteapp.util.Common
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeNoteRepository : NoteRepository {
    override fun getAllNotes(): Flow<List<Note>> = flow {
        emit(Common.notes)
    }

    override suspend fun getNoteById(id: Int): Note? {
        return Common.notes.maxByOrNull { it.id == id }
    }

    override suspend fun insertNote(note: Note) {
    }

    override suspend fun deleteNote(note: Note) {
        Common.notes.remove(note)
    }
}