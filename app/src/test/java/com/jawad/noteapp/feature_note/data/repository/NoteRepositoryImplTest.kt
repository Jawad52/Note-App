package com.jawad.noteapp.feature_note.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.jawad.noteapp.feature_note.data.local.NoteDao
import com.jawad.noteapp.util.Common
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class NoteRepositoryImplTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private var noteDao = mock(NoteDao::class.java)

    @Test
    fun `Should fail when getNotes method is invoked returns exception`() = runTest(testDispatcher) {
        whenever(noteDao.getNotes()) doReturn flow {
            throw Exception()
        }
        val repository = NoteRepositoryImpl(noteDao)
        repository.getAllNotes().test {
            awaitError()
        }
    }

    @Test
    fun `Should success when data insertion into Note database`() = runTest(testDispatcher) {
        val notes = Common.note1
        whenever(noteDao.getNotes()) doReturn flow {
            emit(listOf(notes))
        }
        val repository = NoteRepositoryImpl(noteDao)
        repository.getAllNotes().test {
            val result = awaitItem()
            assertThat(result.contains(notes)).isTrue()
            awaitComplete()
        }
    }

    @Test
    fun `Should fail when getNotesById method is invoked returns null`() = runTest(testDispatcher) {
        val noteId = 0
        whenever(noteDao.getNoteById(noteId)) doReturn null
        val repository = NoteRepositoryImpl(noteDao)

        val result =  repository.getNoteById(noteId)
        assertThat(result).isNull()
    }

    @Test
    fun `Should success when getNotesById method is invoked returns a note`() = runTest(testDispatcher) {
        val note = Common.note1
        val noteId = 1
        whenever(noteDao.getNoteById(noteId)) doReturn note
        val repository = NoteRepositoryImpl(noteDao)

        val result =  repository.getNoteById(noteId)
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo(note)
    }
}