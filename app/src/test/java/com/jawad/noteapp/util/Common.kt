package com.jawad.noteapp.util

import androidx.compose.ui.focus.FocusState
import com.jawad.noteapp.feature_note.domain.model.Note
import java.util.*

class Common {
    companion object {
        val note = Note("Insert text test", "test content", getDates(0).time, 1, 12)

        val note1 = Note("A test", "test content", getDates(0).time, 1, 1)
        val note2 = Note("B test 2", "test content 2", getDates(-5).time, 0, 2)

        val lastNote = Note("E test 5", "test content 5", getDates(-2).time, 2, 5)

        val notes = mutableListOf(
            note1,
            note2,
            Note("C test 3", "test content 3", getDates(-3).time, 1, 3),
            Note("D test 4", "test content 4", getDates(-4).time, 3, 4),
            lastNote
        )

        private fun getDates(int: Int): Date {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DATE, int)
            return cal.time
        }
    }
}

internal enum class FakeFocusStateImpl : FocusState {
    Active,

    // One of the descendants of the focusable component is Active.
    ActiveParent,

    // The focusable component is currently active (has focus), and is in a state where
    // it does not want to give up focus. (Eg. a text field with an invalid phone number).
    Captured,

    // The focusable component is not currently focusable. (eg. A disabled button).
    Deactivated,

    // One of the descendants of this deactivated component is Active.
    DeactivatedParent,

    // The focusable component does not receive any key events. (ie it is not active, nor are any
    // of its descendants active).
    Inactive;

    override val isFocused: Boolean
        get() = when (this) {
            Captured, FakeFocusStateImpl.Active -> true
            ActiveParent, Deactivated, DeactivatedParent, Inactive -> false
        }

    override val hasFocus: Boolean
        get() = when (this) {
            FakeFocusStateImpl.Active, ActiveParent, Captured, DeactivatedParent -> true
            Deactivated, Inactive -> false
        }

    override val isCaptured: Boolean
        get() = when (this) {
            Captured -> true
            FakeFocusStateImpl.Active, ActiveParent, Deactivated, DeactivatedParent, Inactive -> false
        }

    /**
     * Whether the focusable component is deactivated.
     *
     * TODO(ralu): Consider making this public when we can add methods to interfaces without
     * breaking compatibility.
     */
    val isDeactivated: Boolean
        get() = when (this) {
            FakeFocusStateImpl.Active, ActiveParent, Captured, Inactive -> false
            Deactivated, DeactivatedParent -> true
        }
}