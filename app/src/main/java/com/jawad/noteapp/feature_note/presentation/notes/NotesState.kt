package com.jawad.noteapp.feature_note.presentation.notes

import com.jawad.noteapp.feature_note.domain.model.Note
import com.jawad.noteapp.feature_note.domain.util.NoteOrder
import com.jawad.noteapp.feature_note.domain.util.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Title(OrderType.Ascending),
    val isOrderSectionIsVisible: Boolean = false
)
