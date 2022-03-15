package com.jawad.noteapp.feature_note.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jawad.noteapp.feature_note.domain.model.Note

@Database(
    entities = [Note::class],
    version = 1
)
abstract class NoteDatabase: RoomDatabase() {

    abstract val getNoteDao: NoteDao

    companion object {
        val DATA_BASE_NAME = "note_db"
    }
}