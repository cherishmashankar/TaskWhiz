package com.example.taskwhiz.presentation.ui.screen.taskEditorScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import com.example.taskwhiz.R
import com.example.taskwhiz.presentation.ui.theme.AppDimens

@Composable
fun SubtaskList(
    subtasks: List<String>,
    onSubtasksChange: (List<String>) -> Unit
) {
    val items = subtasks.ifEmpty { listOf("") }
    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequesters = remember(items.size) { List(items.size) { FocusRequester() } }
    var focusTrigger by remember { mutableStateOf<Pair<Int, Boolean>?>(null) }

    // Move focus after list updates (add/remove)
    LaunchedEffect(focusTrigger) {
        focusTrigger?.let { (index, isNext) ->
            if (isNext) {
                focusRequesters.getOrNull(index + 1)?.requestFocus()
            } else {
                focusRequesters.getOrNull(index - 1)?.requestFocus()
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(AppDimens.SubtaskRowSpacing),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        items.forEachIndexed { index, text ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppDimens.SubtaskRowPaddingVertical)
                    .clickable {
                        focusRequesters.getOrNull(index)?.requestFocus()
                        keyboard?.show()
                    }
            ) {
                Icon(
                    imageVector = Icons.Filled.FiberManualRecord,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(AppDimens.SubtaskIconSize)
                        .padding(end = AppDimens.SubtaskIconSpacing)
                )

                BasicTextField(
                    value = text,
                    onValueChange = { updated ->
                        val copy = items.toMutableList().apply { this[index] = updated }
                        onSubtasksChange(copy)
                    },
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                    textStyle = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            val isLastItem = index == items.lastIndex
                            if (text.isNotBlank() && isLastItem) {
                                val copy = items.toMutableList().apply { add(index + 1, "") }
                                onSubtasksChange(copy)
                                focusTrigger = index to true
                            } else if (index < items.lastIndex) {
                                focusRequesters.getOrNull(index + 1)?.requestFocus()
                            }
                        }
                    ),
                    decorationBox = { innerTextField ->
                        if (text.isEmpty()) {
                            Text(
                                text = stringResource(R.string.hint_add_subtask),
                                color = Color.Gray.copy(alpha = 0.6f),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                        innerTextField()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequesters[index])
                        .focusProperties {
                            next = focusRequesters.getOrNull(index + 1) ?: FocusRequester.Default
                            previous = focusRequesters.getOrNull(index - 1) ?: FocusRequester.Default
                        }
                        .onKeyEvent {
                            if (it.key == Key.Backspace && it.type == KeyEventType.KeyUp) {
                                if (text.isEmpty() && items.size > 1) {
                                    val copy = items.toMutableList().apply { removeAt(index) }
                                    onSubtasksChange(copy)
                                    focusTrigger = index to false
                                    return@onKeyEvent true
                                }
                            }
                            false
                        }
                )
            }
        }
    }
}



