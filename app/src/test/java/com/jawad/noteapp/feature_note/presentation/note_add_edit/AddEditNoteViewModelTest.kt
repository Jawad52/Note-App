package com.jawad.noteapp.feature_note.presentation.note_add_edit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.jawad.noteapp.feature_note.domain.use_case.*
import com.jawad.noteapp.feature_note.presentation.notes.NotesViewModel
import com.jawad.noteapp.util.Common
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

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
    fun `verify if initial value has text content for valid note id`() =
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

            val noteUseCase = NoteUseCase(getNotesUseCase, deleteNoteUseCase, addNoteUseCase, getNoteByIdUseCase)
            addEditNoteViewModel = AddEditNoteViewModel(noteUseCase, savedStateHandle)

            assertThat(addEditNoteViewModel.getTitleTextFieldState.value.text).isNotEmpty()
        }

    //Should throw error while save with empty content
    //Should success while adding note with valid content

    //Verify if the color changes on color change event
    //Verify if the title hint is visible, when there is no title text
    //Verify if the title hint is hidden, when there is a title text

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}