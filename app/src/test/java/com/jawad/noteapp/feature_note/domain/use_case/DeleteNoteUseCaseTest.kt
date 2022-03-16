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
class DeleteNoteUseCaseTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private var noteRepository = FakeNoteRepository()

    lateinit var deleteNoteUseCase: DeleteNoteUseCase

    @Before
    fun setUp() {
        deleteNoteUseCase = DeleteNoteUseCase(noteRepository)
    }

    @Test
    fun `Should fail when note1 is deleted database should contain note2 not note1`() = runTest(testDispatcher) {
        val delNote = Common.note2
        deleteNoteUseCase(Common.note1)
        assertThat(!Common.notes.contains(delNote)).isFalse()
    }

    @Test
    fun `Should success when note1 is deleted database should not contain note1`() = runTest(testDispatcher) {
        val delNote = Common.note1
        deleteNoteUseCase(delNote)
        assertThat(Common.notes.contains(delNote)).isFalse()
    }
}