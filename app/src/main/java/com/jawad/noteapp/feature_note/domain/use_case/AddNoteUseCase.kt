package com.jawad.noteapp.feature_note.domain.use_case

import com.jawad.noteapp.feature_note.domain.model.InvalidNoteException
import com.jawad.noteapp.feature_note.domain.model.Note
import com.jawad.noteapp.feature_note.domain.repository.NoteRepository
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(private val noteRepository: NoteRepository) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank())
            throw InvalidNoteException("Note title can't be empty")
        if (note.content.isBlank())
            throw InvalidNoteException("Note content can't be empty")

        noteRepository.insertNote(note)
    }
}