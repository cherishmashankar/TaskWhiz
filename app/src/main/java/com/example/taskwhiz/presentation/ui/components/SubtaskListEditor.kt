package com.example.taskwhiz.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController



@Composable
fun SubtaskListEditor(
    subtasks: List<String>,
    onSubtasksChange: (List<String>) -> Unit
) {
    val items = subtasks.ifEmpty { listOf("") }
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEachIndexed { index, text ->
            val focusRequester = remember { FocusRequester() }

            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
                    .clickable {
                        focusRequester.requestFocus()
                        keyboard?.show()
                    }
            ) {
                Icon(
                    imageVector = Icons.Filled.FiberManualRecord,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp)
                )

                BasicTextField(
                    value = text,
                    onValueChange = { updated ->
                        val copy = items.toMutableList().apply { this[index] = updated }
                        onSubtasksChange(copy)
                    },
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            val isLastItem = index == items.lastIndex
                            if (text.isNotBlank() && isLastItem) {
                                val copy = items.toMutableList().apply { add(index + 1, "") }
                                onSubtasksChange(copy)

                                // TODO focus does not move to next point
                                focusManager.moveFocus(FocusDirection.Down)
                            } else {
                                // If not last, just move focus to next existing bullet
                                // TODO focus does not move to next point
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        }
                    ),
                    decorationBox = { innerTextField ->
                        if (text.isEmpty()) {
                            Text(
                                "Add subtask…",
                                color = Color.Gray.copy(alpha = 0.6f),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        innerTextField()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                        .focusable()
                        .onKeyEvent {
                            if (it.key == Key.Backspace
                            ) {
                                if (text.isEmpty() && items.size > 1) {
                                    val copy = items.toMutableList().apply { removeAt(index) }
                                    onSubtasksChange(copy)

                                    // TODO focus does not move to previous point
                                    focusManager.moveFocus(FocusDirection.Up)
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
