package com.jawad.noteapp.feature_note.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
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
class GetNoteByIdUseCaseTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private var noteRepository = FakeNoteRepository()
    lateinit var getNoteByIdUseCase: GetNoteByIdUseCase

    @Before
    fun setUp() {
        getNoteByIdUseCase = GetNoteByIdUseCase(noteRepository)
    }

    @Test
    fun `Should fail when noteId is -1 to getNoteById of repository`() =
        runTest(testDispatcher) {
            val noteId = -1
            val result = getNoteByIdUseCase(noteId)
            assertThat(result).isNull()
        }

    @Test
    fun `Should success when noteId is 1 to getNoteById of repository`() =
        runTest(testDispatcher) {
            val noteId = 1
            val result = getNoteByIdUseCase(noteId)
            assertThat(result).isNotNull()
        }

    @Test
    fun `Verify the noteId and response noteId is same`() =
        runTest(testDispatcher) {
            val noteId = 1
            val result = getNoteByIdUseCase(noteId)
            assertThat(result).isNotNull()
            assertThat(result?.id == noteId).isTrue()
        }
}