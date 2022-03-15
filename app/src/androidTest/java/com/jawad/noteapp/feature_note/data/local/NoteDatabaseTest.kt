package com.jawad.noteapp.feature_note.data.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.jawad.noteapp.feature_note.domain.model.Note
import com.jawad.noteapp.util.Constant
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class NoteDatabaseTest : TestCase() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    lateinit var dao: NoteDao
    lateinit var database: NoteDatabase

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, NoteDatabase::class.java)
            .build()
        dao = database.getNoteDao
    }


    /*
    Test case to insert note in room database
     */
    @Test
    fun testInsertNote() = runTest(testDispatcher) {
        val note = Constant.note1
        dao.insertNote(note)

        dao.getNotes().test {
            assertThat(awaitItem().contains(note)).isTrue()
        }
    }

    /*
    Test case to delete note in room database
    */
    @Test
    fun testDeleteNote() = runTest(testDispatcher) {
        val note = Constant.note1
        dao.insertNote(note)
        dao.deleteNote(note)

        dao.getNotes().test {
            assertThat(awaitItem().contains(note)).isFalse()
        }
    }

    /*
    Test case to insert and update note in room database
    */
    @Test
    fun testUpdateNote() = runTest(testDispatcher) {
        val note1 = Constant.note1
        val note2 = Note("test 2", "test content 2", Date().time, 1, 1)
        dao.insertNote(note1)
        dao.insertNote(note2)

        dao.getNotes().test {
            val result = awaitItem()
            assertThat(result.contains(note1)).isFalse()
            assertThat(result.contains(note2)).isTrue()
        }
    }

    @After
    fun clearUp() {
        database.close()
    }
}