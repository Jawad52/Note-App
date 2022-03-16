package com.jawad.noteapp.feature_note.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.jawad.noteapp.util.Common
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class AddNoteUseCaseTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private var noteRepository = FakeNoteRepository()

    lateinit var addNoteUseCase: AddNoteUseCase

    @Before
    fun setUp() {
        addNoteUseCase = AddNoteUseCase(noteRepository)
    }


    @Test
    fun `Verify if the note insertion is success`() = runTest(testDispatcher) {
        val newNote = Common.note
        addNoteUseCase(newNote)
        assertThat(Common.notes.contains(newNote)).isTrue()
    }
}