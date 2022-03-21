package com.jawad.noteapp.feature_note.presentation.note_add_edit

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jawad.noteapp.feature_note.domain.model.Note
import com.jawad.noteapp.feature_note.presentation.note_add_edit.components.EditTextFields
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    viewMode: AddEditNoteViewModel = hiltViewModel()
) {
    val titleState = viewMode.getContentTextFieldState.value
    val contentState = viewMode.getContentTextFieldState.value

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val noteBackgroundAnimatable = remember {
        Animatable(
            Color(if (noteColor != -1) noteColor else viewMode.noteColor.value)
        )
    }
    LaunchedEffect(key1 = true) {
        viewMode.eventFlow.collectLatest {
            when (it) {
                is AddEditNoteViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }
                is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(it.message)
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Note.noteColor.forEach { color ->
                    val colorInt = color.toArgb()
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .shadow(15.dp, CircleShape)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = 3.dp,
                                color = if (viewMode.noteColor.value == colorInt) Color.Black else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    noteBackgroundAnimatable.animateTo(
                                        targetValue = Color(colorInt),
                                        animationSpec = tween(
                                            delayMillis = 500
                                        )
                                    )
                                }
                                viewMode.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            EditTextFields(
                text = titleState.text,
                hintText = titleState.hint,
                hintVisible = titleState.isHintVisible,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = MaterialTheme.typography.h5,
                onFocusChange = { viewMode.onEvent(AddEditNoteEvent.ChangeTitleFocus(it)) },
                onTextChange = { viewMode.onEvent(AddEditNoteEvent.TitleTextChange(it)) },
            )
            Spacer(modifier = Modifier.height(15.dp))
            EditTextFields(
                text = contentState.text,
                hintText = contentState.hint,
                hintVisible = contentState.isHintVisible,
                modifier = Modifier.fillMaxHeight(),
                textStyle = MaterialTheme.typography.body1,
                onFocusChange = { viewMode.onEvent(AddEditNoteEvent.ChangeContentFocus(it)) },
                onTextChange = { viewMode.onEvent(AddEditNoteEvent.ContentTextChange(it)) },
            )
        }
    }
}