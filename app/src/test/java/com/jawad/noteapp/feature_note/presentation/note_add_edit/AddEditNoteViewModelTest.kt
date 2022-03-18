package com.jawad.noteapp.feature_note.presentation.note_add_edit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class AddEditNoteViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val testCoroutineDispatcher = StandardTestDispatcher()
    lateinit var addEditNoteViewModel: AddEditNoteViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }



    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}