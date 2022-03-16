package com.jawad.noteapp.feature_note.domain.use_case

import com.jawad.noteapp.feature_note.domain.model.Note
import com.jawad.noteapp.feature_note.domain.repository.NoteRepository
import com.jawad.noteapp.feature_note.domain.util.NoteOrder
import com.jawad.noteapp.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNoteUseCase @Inject constructor(private val repository: NoteRepository) {

    operator fun invoke(noteOrder: NoteOrder = NoteOrder.Title(OrderType.AscendingOrderType)): Flow<List<Note>> {
        return repository.getAllNotes().map { notes ->
            when (noteOrder.orderType) {
                is OrderType.AscendingOrderType -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> notes.sortedBy { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedBy { it.timestamp }
                        is NoteOrder.Color -> notes.sortedBy { it.color }
                    }
                }
                is OrderType.DescendingOrderType -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedByDescending { it.timestamp }
                        is NoteOrder.Color -> notes.sortedByDescending { it.color }
                    }
                }
            }
        }
    }
}