package com.jawad.noteapp.feature_note.presentation.note_add_edit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.jawad.noteapp.feature_note.domain.model.InvalidNoteException
import com.jawad.noteapp.feature_note.domain.model.Note
import com.jawad.noteapp.feature_note.domain.use_case.*
import com.jawad.noteapp.feature_note.presentation.notes.NotesViewModel
import com.jawad.noteapp.ui.theme.Violet
import com.jawad.noteapp.util.Common
import com.jawad.noteapp.util.FakeFocusStateImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
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
@RunWith(JUnit4::class)
class AddEditNoteViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val testCoroutineDispatcher = StandardTestDispatcher()
    lateinit var addEditNoteViewModel: AddEditNoteViewModel

    private val getNotesUseCase = mock<GetNoteUseCase>()
    private val deleteNoteUseCase = mock<DeleteNoteUseCase>()
    private val addNoteUseCase = mock<AddNoteUseCase>()
    private var getNoteByIdUseCase = mock<GetNoteByIdUseCase>()

    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setUp() {
        Dispatchers.setMain(testCoroutineDispatcher)
        savedStateHandle = SavedStateHandle().apply {
            set("noteId", -1)
        }
    }


    @Test
    fun `verify if initial value is empty for all text content for add note when note id is -1`() =
        runTest(testCoroutineDispatcher) {
            val noteUseCase =
                NoteUseCase(getNotesUseCase, deleteNoteUseCase, addNoteUseCase, getNoteByIdUseCase)
            addEditNoteViewModel = AddEditNoteViewModel(noteUseCase, savedStateHandle)

            assertThat(addEditNoteViewModel.getTitleTextFieldState.value.text).isEmpty()
            assertThat(addEditNoteViewModel.getContentTextFieldState.value.text).isEmpty()
        }

    @Test
    fun `verify if initial value has title and content for valid note id`() =
        runTest(testCoroutineDispatcher) {
            val note = Common.note
            val savedStateHandle = SavedStateHandle().apply {
                set("noteId", note.id)
            }
            val getNoteByIdUseCase = mock<GetNoteByIdUseCase> {
                on {
                    runBlocking { invoke(note.id!!) }
                } doReturn note
            }

            val noteUseCase =
                NoteUseCase(getNotesUseCase, deleteNoteUseCase, addNoteUseCase, getNoteByIdUseCase)
            addEditNoteViewModel = AddEditNoteViewModel(noteUseCase, savedStateHandle)

            delay(500)
            assertThat(addEditNoteViewModel.getTitleTextFieldState.value.text).isNotEmpty()
            assertThat(addEditNoteViewModel.getTitleTextFieldState.value.text).isEqualTo(note.title)
            assertThat(addEditNoteViewModel.getContentTextFieldState.value.text).isEqualTo(note.content)
        }

    @Test
    fun `Verify if the title hint is visible, when there is no title text`() =
        runTest(testCoroutineDispatcher) {
            val noteUseCase =
                NoteUseCase(getNotesUseCase, deleteNoteUseCase, addNoteUseCase, getNoteByIdUseCase)
            addEditNoteViewModel = AddEditNoteViewModel(noteUseCase, savedStateHandle)
            delay(500)
            assertThat(addEditNoteViewModel.getTitleTextFieldState.value.text).isEmpty()
            assertThat(addEditNoteViewModel.getTitleTextFieldState.value.isHintVisible).isTrue()
        }

    @Test
    fun `Verify if the title hint is hidden, when there is a title text`() =
        runTest(testCoroutineDispatcher) {
            val noteUseCase =
                NoteUseCase(getNotesUseCase, deleteNoteUseCase, addNoteUseCase, getNoteByIdUseCase)
            addEditNoteViewModel = AddEditNoteViewModel(noteUseCase, savedStateHandle)

            addEditNoteViewModel.onEvent(AddEditNoteEvent.TitleTextChange("Title text"))
            val focusState = FakeFocusStateImpl.Active
            addEditNoteViewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(focusState))

            delay(500)
            assertThat(addEditNoteViewModel.getTitleTextFieldState.value.text).isNotEmpty()
            assertThat(addEditNoteViewModel.getTitleTextFieldState.value.isHintVisible).isFalse()
        }

    @Test
    fun `Should throw error while save with empty content`() = runTest(testCoroutineDispatcher) {
        whenever(addNoteUseCase.invoke(any())) doThrow (InvalidNoteException(""))
        val noteUseCase =
            NoteUseCase(getNotesUseCase, deleteNoteUseCase, addNoteUseCase, getNoteByIdUseCase)
        addEditNoteViewModel = AddEditNoteViewModel(noteUseCase, savedStateHandle)

        addEditNoteViewModel.onEvent(AddEditNoteEvent.SaveNote)

        addEditNoteViewModel.eventFlow.test {
            val result = awaitItem()
            assertThat(result).isInstanceOf(AddEditNoteViewModel.UiEvent.ShowSnackbar::class.java)
        }
    }

    @Test
    fun `Should success while adding note with valid content`() = runTest(testCoroutineDispatcher) {
        val noteUseCase =
            NoteUseCase(getNotesUseCase, deleteNoteUseCase, addNoteUseCase, getNoteByIdUseCase)

        addEditNoteViewModel = AddEditNoteViewModel(noteUseCase, savedStateHandle)

        addEditNoteViewModel.onEvent(AddEditNoteEvent.TitleTextChange("Title text"))
        addEditNoteViewModel.onEvent(AddEditNoteEvent.ContentTextChange("Content text"))
        addEditNoteViewModel.onEvent(AddEditNoteEvent.SaveNote)

        addEditNoteViewModel.eventFlow.test {
            val result = awaitItem()
            assertThat(result).isNotInstanceOf(AddEditNoteViewModel.UiEvent.ShowSnackbar::class.java)
            assertThat(result).isInstanceOf(AddEditNoteViewModel.UiEvent.SaveNote::class.java)
        }
    }

    @Test
    fun `Verify if the color changes on color change event`() = runTest(testCoroutineDispatcher) {
        val noteColor = Violet.toArgb()
        val noteUseCase =
            NoteUseCase(getNotesUseCase, deleteNoteUseCase, addNoteUseCase, getNoteByIdUseCase)
        addEditNoteViewModel = AddEditNoteViewModel(noteUseCase, savedStateHandle)

        addEditNoteViewModel.onEvent(AddEditNoteEvent.ChangeColor(noteColor))

        delay(500)
        assertThat(addEditNoteViewModel.noteColor.value).isEqualTo(noteColor)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}