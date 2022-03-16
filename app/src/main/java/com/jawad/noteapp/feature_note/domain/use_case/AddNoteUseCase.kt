package com.jawad.noteapp.feature_note.domain.use_case

import com.jawad.noteapp.feature_note.domain.model.Note
import com.jawad.noteapp.feature_note.domain.repository.NoteRepository
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(note: Note) {
        noteRepository.insertNote(note)
    }
}