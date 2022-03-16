package com.jawad.noteapp.util

import com.jawad.noteapp.feature_note.domain.model.Note
import java.util.*

class Common {
    companion object {
        val note1 = Note("test", "test content", Date().time, 1, 1)
        val note2 = Note("test 2", "test content 2", Date().time, 0, 2)
        val notes = listOf(
            note1,
            note2,
            Note("test 3", "test content 3", Date().time, 1, 3),
            Note("test 4", "test content 4", Date().time, 3, 4),
            Note("test 5", "test content 5", Date().time, 2, 5)
        )
    }
}