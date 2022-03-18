package com.jawad.noteapp.feature_note.presentation.notes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.jawad.noteapp.feature_note.domain.use_case.AddNoteUseCase
import com.jawad.noteapp.feature_note.domain.use_case.DeleteNoteUseCase
import com.jawad.noteapp.feature_note.domain.use_case.GetNoteUseCase
import com.jawad.noteapp.feature_note.domain.use_case.NoteUseCase
import com.jawad.noteapp.feature_note.domain.util.NoteOrder
import com.jawad.noteapp.feature_note.domain.util.OrderType
import com.jawad.noteapp.util.Common
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
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

            assertThat(notesViewModel.noteState.value).isNotNull()
        }

    @Test
    fun `Verify all the state is non-null`() =
        runTest(testCoroutineDispatcher) {
            val getNoteUseCase = mock<GetNoteUseCase>()
            whenever(getNoteUseCase(any())) doReturn flow { emit(Common.notes) }

            val noteUseCase = NoteUseCase(getNoteUseCase, deleteNoteUseCase, addNoteUseCase)
            val notesViewModel = NotesViewModel(noteUseCase)
            val result = notesViewModel.noteState.value
            assertThat(result).isNotNull()
            assertThat(result.notes).isNotNull()
            assertThat(result.noteOrder).isNotNull()
            assertThat(result.isOrderSectionIsVisible).isNotNull()
        }

    @Test
    fun `Verify the if the ToggleOrderSection event for inverts when user NoteEvent_ToggleOrderSection invoked`() =
        runTest(testCoroutineDispatcher) {
            val noteUseCase = NoteUseCase(getNotesUseCase, deleteNoteUseCase, addNoteUseCase)
            val notesViewModel = NotesViewModel(noteUseCase)

            notesViewModel.onEvent(NoteEvent.ToggleOrderSection)
            val finalResult = notesViewModel.noteState.value
            assertThat(finalResult.isOrderSectionIsVisible).isTrue()
        }

    @Test
    fun `Should sort in descending order when the order event is changed to Descending order`() =
        runTest(testCoroutineDispatcher) {
            val getNoteUseCase = mock<GetNoteUseCase>()
            whenever(getNoteUseCase(any())) doReturn flow { emit(Common.notes) }

            val noteUseCase = NoteUseCase(getNoteUseCase, deleteNoteUseCase, addNoteUseCase)
            val notesViewModel = NotesViewModel(noteUseCase)

            notesViewModel.onEvent(NoteEvent.OrderNote(NoteOrder.Title(OrderType.Descending)))
            delay(100)
            val finalResult = notesViewModel.noteState.value
            assertThat(finalResult.noteOrder.orderType == OrderType.Ascending).isFalse()
            assertThat(finalResult.noteOrder.orderType == OrderType.Descending).isTrue()
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}