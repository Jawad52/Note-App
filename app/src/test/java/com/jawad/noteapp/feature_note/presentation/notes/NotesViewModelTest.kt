package com.jawad.noteapp.feature_note.presentation.notes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.jawad.noteapp.feature_note.domain.use_case.AddNoteUseCase
import com.jawad.noteapp.feature_note.domain.use_case.DeleteNoteUseCase
import com.jawad.noteapp.feature_note.domain.use_case.GetNoteUseCase
import com.jawad.noteapp.feature_note.domain.use_case.NoteUseCase
import com.jawad.noteapp.util.Common
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@RunWith(JUnit4::class)
class NotesViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val testCoroutineDispatcher = StandardTestDispatcher()

    private val getNotesUseCase = mock<GetNoteUseCase>()
    private val deleteNoteUseCase = mock<DeleteNoteUseCase>()
    private val addNoteUseCase = mock<AddNoteUseCase>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @Test
    fun `Verify the note state is not null when the note view model is initiate`() =
        runTest(testCoroutineDispatcher) {
            val noteUseCase = NoteUseCase(getNotesUseCase, deleteNoteUseCase, addNoteUseCase)
            val notesViewModel = NotesViewModel(noteUseCase)

            notesViewModel.noteState.test {
                assertThat(awaitItem()).isNotNull()
            }
        }

    @Test
    fun `Should return note list when the view model is initiate`() =
        runTest(testCoroutineDispatcher) {
            val getNoteUseCase = mock<GetNoteUseCase>()
            whenever(getNoteUseCase(any())) doReturn flow { emit(Common.notes) }

            val noteUseCase = NoteUseCase(getNoteUseCase, deleteNoteUseCase, addNoteUseCase)
            val notesViewModel = NotesViewModel(noteUseCase)
            notesViewModel.noteState.test {
                awaitItem()
                val result = awaitItem()
                assertThat(result).isNotNull()
                assertThat(result.notes).isNotEmpty()
            }
        }

    @Test
    fun `Verify if the ToggleOrder inverse's the value when on onEvent is called for ToggleOrderSection event`() =
        runTest(testCoroutineDispatcher) {
            val noteUseCase = NoteUseCase(getNotesUseCase, deleteNoteUseCase, addNoteUseCase)
            val notesViewModel = NotesViewModel(noteUseCase)

            val initialToggleOrder = notesViewModel.noteState.value.isOrderSectionIsVisible
            notesViewModel.onEvent(NoteEvent.ToggleOrderSection)

            notesViewModel.noteState.test {
                val result = awaitItem()
                assertThat(result).isNotNull()
                assertThat(result.isOrderSectionIsVisible).isNotEqualTo(initialToggleOrder)
            }
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}