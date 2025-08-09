package com.example.taskwhiz.presentation.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.taskwhiz.domain.model.Task


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

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.fillMaxSize(),
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(modifier = Modifier.weight(1f) ) {
                    TaskTitleInput(
                        title = title,
                        onTitleChange = { title = it }
                    )
                }

/*                TaskEditorOverflowMenu(
                    hasDue = dueDate != null,
                    hasReminder = reminderDate != null,
                    onRequestSetDueDate = { showDatePickerForDue = true },
                    onRequestSetReminder = { showDatePickerForReminder = true },
                    onClearDueDate = { dueDate = null },
                    onClearReminder = { reminderDate = null }
                )*/
            }

            Spacer(Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                ColorPaletteSelector(
                    selectedColor = selectedColor,
                    onColorChange = { selectedColor = it }
                )

                Spacer(Modifier.height(20.dp))

                SubtaskListEditor(
                    subtasks = subtasks,
                    onSubtasksChange = { subtasks = it }
                )
                Spacer(Modifier.height(32.dp))


            }

            Spacer(Modifier.height(16.dp))

            ScheduleSection(
                dueDate = dueDate,
                reminderDate = reminderDate,
                onSetDueClick = { showDatePickerForDue = true },
                onClearDue = { dueDate = null },
                onSetReminderClick = { showDatePickerForReminder = true },
                onClearReminder = { reminderDate = null },
                modifier = Modifier.fillMaxWidth()
            )


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
                        priorityLevel = task?.priorityLevel ?: 0,
                        dueAt = dueDate,
                        reminderAt = reminderDate
                    )
                    if (task == null) onSave(taskToHandle) else onUpdate(taskToHandle)
                },
                enabled = title.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(selectedColor.toColorInt()),
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (task == null) "Save Task" else "Update Task",
                    color = if (title.isNotBlank()) Color.White
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }


            if (showTitleError && title.isBlank()) {
                Text(
                    text = "Title cannot be empty",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

        }



        // Due Date picker (date only) — own state each time
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
                    ) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePickerForDue = false }) { Text("Cancel") }
                }
            ) {
                DatePicker(
                    state = duePickerState,
                    showModeToggle = true
                )
            }
        }

        // Reminder flow: pick date, then time
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
                    ) { Text("Next") }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePickerForReminder = false }) { Text("Cancel") }
                }
            ) {
                DatePicker(
                    state = reminderPickerState,
                    showModeToggle = true
                )
            }
        }

        if (showTimePicker && pendingReminderDate != null) {
            OpenTimePicker(
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


