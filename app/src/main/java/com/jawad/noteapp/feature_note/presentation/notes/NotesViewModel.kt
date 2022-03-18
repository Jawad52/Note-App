package com.jawad.noteapp.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jawad.noteapp.feature_note.domain.model.Note
import com.jawad.noteapp.feature_note.domain.use_case.NoteUseCase
import com.jawad.noteapp.feature_note.domain.util.NoteOrder
import com.jawad.noteapp.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val noteUseCase: NoteUseCase) : ViewModel() {

    private val _noteState = mutableStateOf(NotesState())
    val noteState: State<NotesState> = _noteState

    private var lastDeletedNote: Note? = null
    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Title(OrderType.Ascending))
    }

    /**
     * Fetch all notes form database
     */
    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCase.getNoteUseCase(noteOrder).onEach { notes ->
            _noteState.value = noteState.value.copy(
                notes = notes,
                noteOrder = noteOrder
            )
        }.launchIn(viewModelScope)
    }

    /**
     * This method reacts on user interaction
     */
    fun onEvent(noteEvent: NoteEvent) {
        when (noteEvent) {
            is NoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCase.deleteNoteUseCase(noteEvent.note)
                    lastDeletedNote = noteEvent.note
                }
            }
            is NoteEvent.OrderNote -> {
                if (noteState.value.noteOrder::class == noteEvent.noteOrder::class &&
                    noteState.value.noteOrder.orderType == noteEvent.noteOrder.orderType
                ) {
                    return
                }
                getNotes(noteEvent.noteOrder)
            }
            is NoteEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCase.addNoteUseCase(lastDeletedNote ?: return@launch)
                    lastDeletedNote = null
                }
            }
            is NoteEvent.ToggleOrderSection -> {
                _noteState.value = noteState.value.copy(
                    isOrderSectionIsVisible = !noteState.value.isOrderSectionIsVisible
                )
            }
        }
    }
}