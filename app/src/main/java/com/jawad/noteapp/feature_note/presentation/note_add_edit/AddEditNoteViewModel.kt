package com.jawad.noteapp.feature_note.presentation.note_add_edit

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jawad.noteapp.feature_note.domain.model.InvalidNoteException
import com.jawad.noteapp.feature_note.domain.model.Note
import com.jawad.noteapp.feature_note.domain.use_case.NoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCase: NoteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var _titleTextFieldState = mutableStateOf(NoteTextFieldState(hint = "Enter title"))
    var getTitleTextFieldState: State<NoteTextFieldState> = _titleTextFieldState

    private var _contentTextFieldState = mutableStateOf(NoteTextFieldState(hint = "Enter content"))
    var getContentTextFieldState: State<NoteTextFieldState> = _contentTextFieldState

    private val _noteColor = mutableStateOf(Note.noteColor.random().toArgb())
    val noteColor: State<Int> = _noteColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCase.getNoteByIdUseCase(noteId)?.let { note ->
                        currentNoteId = noteId
                        _titleTextFieldState.value = getTitleTextFieldState.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _contentTextFieldState.value = getContentTextFieldState.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        _noteColor.value = note.color
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.TitleTextChange -> {
                _titleTextFieldState.value = getTitleTextFieldState.value.copy(
                    text = event.text
                )
            }
            is AddEditNoteEvent.ContentTextChange -> {
                _titleTextFieldState.value = getContentTextFieldState.value.copy(
                    text = event.text
                )
            }
            is AddEditNoteEvent.ChangeContentFocus -> {
                _contentTextFieldState.value = getContentTextFieldState.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            getContentTextFieldState.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.ChangeTitleFocus -> {
                _titleTextFieldState.value = getTitleTextFieldState.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            getTitleTextFieldState.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = event.color
            }
            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCase.addNoteUseCase(
                            Note(
                                title = getTitleTextFieldState.value.text,
                                content = getContentTextFieldState.value.text,
                                color = noteColor.value,
                                id = currentNoteId,
                                timestamp = System.currentTimeMillis()
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidNoteException) {
                        _eventFlow.emit(UiEvent.ShowSnackbar(e.message ?: "Failed to add note"))
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }
}