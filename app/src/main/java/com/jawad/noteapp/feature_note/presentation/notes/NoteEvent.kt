package com.jawad.noteapp.feature_note.presentation.notes

import com.jawad.noteapp.feature_note.domain.model.Note
import com.jawad.noteapp.feature_note.domain.util.NoteOrder

sealed class NoteEvent {
    data class DeleteNote(val note: Note) : NoteEvent()
    data class OrderNote(val noteOrder: NoteOrder) : NoteEvent()
    object RestoreNote : NoteEvent()
    object ToggleOrderSection : NoteEvent()
}
