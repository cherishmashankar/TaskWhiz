package com.example.taskwhiz.presentation.ui.screen.taskEditorScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskwhiz.R
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.presentation.ui.theme.AppDimens
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.taskwhiz.presentation.ui.screen.taskEditorScreen.components.ColorPaletteSelector
import com.example.taskwhiz.presentation.ui.screen.taskEditorScreen.components.SubtaskList
import com.example.taskwhiz.presentation.ui.screen.taskEditorScreen.components.TaskEditorMoreOptionSection
import com.example.taskwhiz.presentation.ui.screen.taskEditorScreen.components.TaskEditorOverflowMenu
import com.example.taskwhiz.presentation.ui.screen.taskEditorScreen.components.TaskTitleInput
import com.example.taskwhiz.presentation.ui.screen.taskEditorScreen.components.TimePickerDialog
import kotlinx.coroutines.android.awaitFrame

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditorScreen(
    task: Task? = null,
    onSave: (Task) -> Unit,
    onDismiss: () -> Unit,
    onUpdate: (Task) -> Unit,
) {

    var title by rememberSaveable(task?.id) { mutableStateOf(task?.title ?: "") }
    var selectedColor by rememberSaveable(task?.id) { mutableStateOf(task?.colorCode ?: "#FF6F61") }
    var subtasks by rememberSaveable(task?.id) { mutableStateOf(task?.taskItems ?: listOf("")) }
    var dueDate by rememberSaveable(task?.id) { mutableStateOf<Long?>(task?.dueAt) }
    var reminderDate by rememberSaveable(task?.id) { mutableStateOf<Long?>(task?.reminderAt) }
    var taskPriority by rememberSaveable(task?.id) { mutableStateOf(task?.priorityLevel ?: 0) }

    var showDatePickerForDue by remember { mutableStateOf(false) }
    var showDatePickerForReminder by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var pendingReminderDate by remember { mutableStateOf<Long?>(null) }
    var showTitleError by remember { mutableStateOf(false) }

    // Shared Task Builder
    fun buildTask(): Task = Task(
        id = task?.id ?: 0L,
        title = title.trim(),
        colorCode = selectedColor,
        taskItems = subtasks.filter { it.isNotBlank() },
        createdAt = task?.createdAt ?: System.currentTimeMillis(),
        isMessy = task?.isMessy ?: false,
        priorityLevel = taskPriority,
        dueAt = dueDate,
        reminderAt = reminderDate
    )

    Scaffold(
        topBar = {
            val buttonTextStyle = MaterialTheme.typography.titleMedium.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            TopAppBar(
                title = {},
                navigationIcon = {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = stringResource(R.string.cancel),
                            style = buttonTextStyle
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            if (title.isBlank()) {
                                showTitleError = true
                                return@TextButton
                            }
                            val built = buildTask()
                            if (task == null) onSave(built) else onUpdate(built)
                        },
                        enabled = title.isNotBlank(),
                        modifier = Modifier.padding(top = 12.dp)
                    ) {
                        Text(
                            text = if (task == null) stringResource(R.string.add_task) else stringResource(R.string.update_task),
                            style = buttonTextStyle,
                            color = if (title.isNotBlank()) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)

                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = AppDimens.PaddingLarge)
                .verticalScroll(rememberScrollState())
        ) {

            val titleFocusRequester = remember { FocusRequester() }
            val keyboard = LocalSoftwareKeyboardController.current

            LaunchedEffect(Unit) {
                if ( task == null) {
                    awaitFrame()
                    titleFocusRequester.requestFocus()
                    keyboard?.show()
                }
            }

            Spacer(Modifier.height(AppDimens.PaddingMedium))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = AppDimens.PaddingMedium, vertical = AppDimens.PaddingMedium)
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





            if (showTitleError && title.isBlank()) {
                Text(
                    text = stringResource(R.string.error_empty_title),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(Modifier.height(AppDimens.PaddingLarge))
            Card(
                shape = RoundedCornerShape(AppDimens.CornerLarge),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                ColorPaletteSelector(
                    selectedColor = selectedColor,
                    onColorChange = { selectedColor = it }
                )
            }

                Spacer(Modifier.height(AppDimens.PaddingLarge))

                TaskEditorMoreOptionSection(
                    dueDate = dueDate,
                    reminderDate = reminderDate,
                    isHighPriority = (taskPriority == 1),
                    onSetDueClick = { showDatePickerForDue = true },
                    onSetReminderClick = { showDatePickerForReminder = true },
                    modifier = Modifier.fillMaxWidth()
                )


            Spacer(Modifier.height(AppDimens.PaddingLarge))

            SubtaskList(
                subtasks = subtasks,
                onSubtasksChange = { subtasks = it }
            )
            Spacer(Modifier.height(AppDimens.PaddingXLarge))

        }
    }

    // --- Date/Time Pickers ---
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

