package com.jawad.noteapp.feature_note.presentation.note_add_edit.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun EditTextFields(
    text: String,
    hintText: String,
    hintVisible: Boolean = true,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle(),
    onFocusChange: (FocusState) -> Unit,
    onTextChange: (String) -> Unit,
    singleLine: Boolean = false
) {
    Box(
        modifier = modifier
    ) {
        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            singleLine = singleLine,
            textStyle = textStyle,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    onFocusChange(it)
                }
        )
        if (hintVisible) {
            Text(text = hintText, style = textStyle, color = Color.DarkGray)
        }
    }
}