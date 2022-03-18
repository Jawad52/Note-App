package com.jawad.noteapp.feature_note.domain.use_case

data class NoteUseCase(
    val getNoteUseCase: GetNoteUseCase,
    val deleteNoteUseCase: DeleteNoteUseCase,
    val addNoteUseCase: AddNoteUseCase,
    val getNoteByIdUseCase: GetNoteByIdUseCase
)
