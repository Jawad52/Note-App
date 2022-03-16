package com.jawad.noteapp.feature_note.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.jawad.noteapp.feature_note.domain.util.NoteOrder
import com.jawad.noteapp.feature_note.domain.util.OrderType
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
import org.mockito.kotlin.any

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class GetNoteUseCaseTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private var noteRepository = FakeNoteRepository()

    lateinit var getNoteUseCase: GetNoteUseCase

    @Before
    fun setUp() {
        getNoteUseCase = GetNoteUseCase(noteRepository)
    }

    @Test
    fun `Verify note list is not empty list`() = runTest(testDispatcher) {
        getNoteUseCase().test {
            val result = awaitItem()
            assertThat(result).isNotEmpty()
            awaitComplete()
        }
    }

    @Test
    fun `Verify note list is in ascending order by title`() = runTest(testDispatcher) {
        val noteOrder = NoteOrder.Title(OrderType.Ascending)
        getNoteUseCase(noteOrder).test {
            val result = awaitItem()
            assertThat(result).isNotEmpty()
            assertThat(result).containsAnyOf(Common.note1, any())
            awaitComplete()
        }
    }

    @Test
    fun `Verify note list is in descending order by title`() = runTest(testDispatcher) {
        val noteOrder = NoteOrder.Title(OrderType.Descending)
        getNoteUseCase(noteOrder).test {
            val result = awaitItem()
            assertThat(result).isNotEmpty()
            assertThat(result).containsAnyOf(Common.lastNote, any())
            awaitComplete()
        }
    }

    @Test
    fun `Verify if notes list are ordered by color`() = runTest(testDispatcher) {
        val noteOrder = NoteOrder.Color(OrderType.Ascending)
        getNoteUseCase(noteOrder).test {
            val result = awaitItem()
            assertThat(result).isNotEmpty()
            assertThat(result).containsAnyOf(Common.note2, any())
            awaitComplete()
        }
    }

    @Test
    fun `Verify if notes list are ordered by date`() = runTest(testDispatcher) {
        val noteOrder = NoteOrder.Date(OrderType.Ascending)
        getNoteUseCase(noteOrder).test {
            val result = awaitItem()
            assertThat(result).isNotEmpty()
            assertThat(result).containsAnyOf(Common.note2, any())
            awaitComplete()
        }
    }
}