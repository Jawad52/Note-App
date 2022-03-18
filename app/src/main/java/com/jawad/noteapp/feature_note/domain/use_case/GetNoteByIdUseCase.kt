package com.jawad.noteapp.feature_note.domain.use_case

import com.jawad.noteapp.feature_note.domain.model.Note
import com.jawad.noteapp.feature_note.domain.repository.NoteRepository
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(private val repository: NoteRepository) {

    suspend operator fun invoke(noteId: Int): Note? {
        return repository.getNoteById(noteId)
    }
}