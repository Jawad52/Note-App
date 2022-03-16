package com.jawad.noteapp.util

import com.jawad.noteapp.feature_note.domain.model.Note
import java.util.*

class Common {
    companion object {
        val note1 = Note("A test", "test content", getDates(0).time, 1, 1)
        val note2 = Note("B test 2", "test content 2", getDates(-5).time, 0, 2)
        val lastNote = Note("E test 5", "test content 5", getDates(-2).time, 2, 5)
        val notes = listOf(
            note1,
            note2,
            Note("C test 3", "test content 3", getDates(-3).time, 1, 3),
            Note("D test 4", "test content 4", getDates(-4).time, 3, 4),
            lastNote
        )

        private fun getDates(int: Int): Date {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DATE, int)
            return cal.time
        }
    }
}