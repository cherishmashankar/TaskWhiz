package com.example.taskwhiz.presentation.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.taskwhiz.R
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.presentation.ui.theme.AppDimens
import kotlinx.coroutines.android.awaitFrame


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditorBottomSheet(
    task: Task? = null,
    onSave: (Task) -> Unit,
    onDismiss: () -> Unit,
    onUpdate: (Task) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var title by remember { mutableStateOf(task?.title ?: "") }
    var selectedColor by remember { mutableStateOf(task?.colorCode ?: "#9B6DFF") }
    var subtasks by remember { mutableStateOf(task?.taskItems ?: listOf("")) }

    var dueDate by remember { mutableStateOf<Long?>(task?.dueAt) }
    var reminderDate by remember { mutableStateOf<Long?>(task?.reminderAt) }

    var showDatePickerForDue by remember { mutableStateOf(false) }
    var showDatePickerForReminder by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var pendingReminderDate by remember { mutableStateOf<Long?>(null) }

    var showTitleError by remember { mutableStateOf(false) }
    var taskPriority by remember { mutableStateOf(task?.priorityLevel ?: 0) } // 0=no, 1=high

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.fillMaxSize(),
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        val titleFocusRequester = remember { FocusRequester() }
        val keyboard = LocalSoftwareKeyboardController.current

        LaunchedEffect(sheetState.isVisible) {
            if (sheetState.isVisible && task == null) {
                awaitFrame()
                titleFocusRequester.requestFocus()
                keyboard?.show()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .navigationBarsPadding()
                .padding(
                    horizontal = AppDimens.PaddingLarge,
                    vertical = AppDimens.PaddingMedium
                )
        ) {
            val listState = rememberLazyListState()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                TaskTitleInput(
                    title = title,
                    onTitleChange = { title = it },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(titleFocusRequester),
                    selectedColor = selectedColor
                )

                TaskEditorOverflowMenu(
                    hasDue = dueDate != null,
                    hasReminder = reminderDate != null,
                    onRequestSetDueDate = { showDatePickerForDue = true },
                    onRequestSetReminder = { showDatePickerForReminder = true },
                    onClearDueDate = { dueDate = null },
                    onClearReminder = { reminderDate = null },
                    hasHighPriority = (taskPriority == 1),
                    onSetHighPriority = { taskPriority = 1 },
                    onClearPriority = { taskPriority = 0 }
                )
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(AppDimens.PaddingLarge),
                contentPadding = PaddingValues(bottom = AppDimens.PaddingMedium)
            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(AppDimens.CornerLarge),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Spacer(Modifier.height(AppDimens.PaddingLarge))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(AppDimens.PaddingLarge),
                            verticalArrangement = Arrangement.spacedBy(AppDimens.PaddingLarge)
                        ) {
                            ColorPaletteSelector(
                                selectedColor = selectedColor,
                                onColorChange = { selectedColor = it }
                            )
                            TaskEditorMoreOptionSection(
                                dueDate = dueDate,
                                reminderDate = reminderDate,
                                isHighPriority = (taskPriority == 1),
                                onSetDueClick = { showDatePickerForDue = true },
                                onSetReminderClick = { showDatePickerForReminder = true },
                                modifier = Modifier.fillMaxWidth()
                            )
                            SubtaskList(
                                subtasks = subtasks,
                                onSubtasksChange = { subtasks = it }
                            )
                            Spacer(Modifier.height(AppDimens.PaddingLarge))
                        }
                    }
                }
            }

            Button(
                onClick = {
                    if (title.isBlank()) {
                        showTitleError = true
                        return@Button
                    }
                    val taskToHandle = Task(
                        id = task?.id ?: 0L,
                        title = title.trim(),
                        colorCode = selectedColor,
                        taskItems = subtasks.filter { it.isNotBlank() },
                        createdAt = task?.createdAt ?: System.currentTimeMillis(),
                        isMessy = task?.isMessy ?: true,
                        priorityLevel = taskPriority,
                        dueAt = dueDate,
                        reminderAt = reminderDate
                    )
                    if (task == null) onSave(taskToHandle) else onUpdate(taskToHandle)
                },
                enabled = title.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppDimens.PaddingSmall),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(selectedColor.toColorInt()),
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(AppDimens.CornerMedium)
            ) {
                Text(
                    text = if (task == null)
                        stringResource(R.string.save_task)
                    else
                        stringResource(R.string.update_task),
                    color = if (title.isNotBlank()) Color.White
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            if (showTitleError && title.isBlank()) {
                Text(
                    text = stringResource(R.string.error_empty_title),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = AppDimens.PaddingSmall)
                )
            }
        }

        // --- Date Pickers ---
        if (showDatePickerForDue) {
            val duePickerState = rememberDatePickerState(
                initialSelectedDateMillis = dueDate ?: System.currentTimeMillis()
            )
            DatePickerDialog(
                onDismissRequest = { showDatePickerForDue = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            dueDate = duePickerState.selectedDateMillis
                            showDatePickerForDue = false
                        }
                    ) { Text(stringResource(R.string.ok)) }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePickerForDue = false }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            ) {
                DatePicker(state = duePickerState, showModeToggle = true)
            }
        }

        if (showDatePickerForReminder) {
            val reminderPickerState = rememberDatePickerState(
                initialSelectedDateMillis = reminderDate ?: System.currentTimeMillis()
            )
            DatePickerDialog(
                onDismissRequest = { showDatePickerForReminder = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            pendingReminderDate = reminderPickerState.selectedDateMillis
                            showDatePickerForReminder = false
                            if (pendingReminderDate != null) showTimePicker = true
                        }
                    ) { Text(stringResource(R.string.next)) }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePickerForReminder = false }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            ) {
                DatePicker(state = reminderPickerState, showModeToggle = true)
            }
        }

        if (showTimePicker && pendingReminderDate != null) {
            TimePickerDialog(
                selectedDateMillis = pendingReminderDate!!,
                onTimeSelected = { finalMillis -> reminderDate = finalMillis },
                onDismiss = {
                    showTimePicker = false
                    pendingReminderDate = null
                }
            )
        }
    }
}


