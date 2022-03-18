package com.jawad.noteapp.feature_note.presentation.note_add_edit

import androidx.compose.ui.focus.FocusState

sealed class AddEditNoteEvent {
    data class TitleTextChange(val text: String) : AddEditNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : AddEditNoteEvent()
    data class ContentTextChange(val text: String) : AddEditNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState) : AddEditNoteEvent()
    data class ChangeColor(val color: Int) : AddEditNoteEvent()
    object SaveNote : AddEditNoteEvent()
}
