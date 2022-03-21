package com.jawad.noteapp.feature_note.presentation.note_add_edit

data class NoteTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
)