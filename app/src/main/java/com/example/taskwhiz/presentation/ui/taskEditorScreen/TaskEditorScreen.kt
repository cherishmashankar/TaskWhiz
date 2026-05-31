package com.example.taskwhiz.presentation.ui.taskEditorScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.taskwhiz.R
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.presentation.ui.theme.AppDimens
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskwhiz.presentation.ui.taskEditorScreen.components.ColorPaletteSelector
import com.example.taskwhiz.presentation.ui.taskEditorScreen.components.SubtaskList
import com.example.taskwhiz.presentation.ui.taskEditorScreen.components.TaskEditorMoreOptionSection
import com.example.taskwhiz.presentation.ui.taskEditorScreen.components.TaskEditorOverflowMenu
import com.example.taskwhiz.presentation.ui.taskEditorScreen.components.TaskTitleInput
import com.example.taskwhiz.presentation.ui.taskEditorScreen.components.TimePickerDialog
import com.example.taskwhiz.presentation.utils.PermissionHandler
import com.example.taskwhiz.presentation.utils.findActivity
import kotlinx.coroutines.android.awaitFrame
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditorScreen(
    onBack: () -> Unit,
    viewModel: TaskEditorViewModel = hiltViewModel()

) {

    // Needs to be refactored
    val task by viewModel.task.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val activity = LocalContext.current.findActivity()

    var hasExactAlarmPermission by remember {
        mutableStateOf(
            PermissionHandler.hasExactAlarmPermission(context)
        )
    }

    var hasNotificationPermission by remember {
        mutableStateOf(
            PermissionHandler.hasNotificationPermission(context)
        )
    }

    DisposableEffect(lifecycleOwner, context) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasExactAlarmPermission =
                    PermissionHandler.hasExactAlarmPermission(context)
                hasNotificationPermission =
                    PermissionHandler.hasNotificationPermission(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    var title by rememberSaveable(task?.id) { mutableStateOf(task?.title ?: "") }
    var selectedColor by rememberSaveable(task?.id) { mutableStateOf(task?.colorCode ?: "#FF6F61") }
    var subtasks by rememberSaveable(task?.id) { mutableStateOf(task?.taskItems ?: listOf("")) }
    var dueDate by rememberSaveable(task?.id) { mutableStateOf<Long?>(task?.dueAt) }
    var reminderDate by rememberSaveable(task?.id) { mutableStateOf<Long?>(task?.reminderAt) }
    var taskPriority by rememberSaveable(task?.id) { mutableIntStateOf(task?.priorityLevel ?: 0) }

    var showDatePickerForDue by remember { mutableStateOf(false) }
    var showDatePickerForReminder by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var pendingReminderDate by remember { mutableStateOf<Long?>(null) }
    var showTitleError by remember { mutableStateOf(false) }

    val dateValidator = remember {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                // Allow dates from the start of today onwards
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                return utcTimeMillis >= calendar.timeInMillis
            }

            override fun isSelectableYear(year: Int): Boolean {
                return year >= Calendar.getInstance().get(Calendar.YEAR)
            }
        }
    }


    fun buildTask(): Task = Task(
        id = task?.id ?: 0L,
        title = title.trim(),
        colorCode = selectedColor,
        taskItems = subtasks.filter { it.isNotBlank() },
        createdAt = task?.createdAt ?: System.currentTimeMillis(),
        isAIGenerated = task?.isAIGenerated ?: false,
        priorityLevel = taskPriority,
        dueAt = dueDate,
        reminderAt = reminderDate
    )

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .padding(horizontal = AppDimens.PaddingMedium)
                    .background(MaterialTheme.colorScheme.background),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background, // Your #0D0D0D
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                title = { },
                navigationIcon = {
                    FilledTonalIconButton(
                        onClick = { onBack() },
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel")
                    }
                },
                actions = {
                    FilledTonalIconButton(
                        onClick = {
                            val built = buildTask()
                            viewModel.saveTask(built)
                            onBack() },
                        enabled = title.isNotBlank(),
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        ),

                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save"
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
            Spacer(Modifier.height(AppDimens.PaddingLarge))
            Card(
                shape = RoundedCornerShape(AppDimens.CornerSmall),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ){
                Row(
                verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = AppDimens.CornerSmall)
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
                    onRequestSetReminder = {
                        if (hasExactAlarmPermission && hasNotificationPermission) {
                            showDatePickerForReminder = true
                        } else {
                            if (!hasNotificationPermission) {
                                PermissionHandler.requestNotificationPermission(activity)
                            }

                            if (!hasExactAlarmPermission) {
                                PermissionHandler.requestExactAlarmPermission(context)
                            }
                        } },
                    onClearDueDate = { dueDate = null },
                    onClearReminder = { reminderDate = null },
                    hasHighPriority = (taskPriority == 1),
                    onSetHighPriority = { taskPriority = 1 },
                    onClearPriority = { taskPriority = 0 }
                )
            }}

            if (showTitleError && title.isBlank()) {
                Text(
                    text = stringResource(R.string.error_empty_title),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(Modifier.height(AppDimens.PaddingLarge))
//            Card(
//                shape = RoundedCornerShape(AppDimens.CornerLarge),
//                colors = CardDefaults.cardColors(
//                    containerColor = MaterialTheme.colorScheme.background
//                ),
//                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//
//                    ColorPaletteSelector(
//                        selectedColor = selectedColor,
//                        onColorChange = { selectedColor = it }
//                    )
//            }
//                Spacer(Modifier.height(AppDimens.PaddingLarge))

                TaskEditorMoreOptionSection(
                    dueDate = dueDate,
                    reminderDate = reminderDate,
                    isHighPriority = (taskPriority == 1),
                    onSetDueClick = { showDatePickerForDue = true },
                    onSetReminderClick = {
                        if (hasExactAlarmPermission && hasNotificationPermission) {
                        showDatePickerForReminder = true
                    } else {
                            if (!hasNotificationPermission) {
                                PermissionHandler.requestNotificationPermission(activity)
                            }

                            if (!hasExactAlarmPermission) {
                                PermissionHandler.requestExactAlarmPermission(context)
                            }
                    }},
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
            initialSelectedDateMillis = dueDate ?: System.currentTimeMillis(),
            selectableDates = dateValidator
        )
        DatePickerDialog(
            onDismissRequest = { },
            confirmButton = {
                TextButton(
                    onClick = {
                        dueDate = duePickerState.selectedDateMillis
                    }
                ) { Text(stringResource(R.string.ok)) }
            },
            dismissButton = {
                TextButton(onClick = { }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = duePickerState, showModeToggle = true)
        }
    }

    if (showDatePickerForReminder) {
        val reminderPickerState = rememberDatePickerState(
            initialSelectedDateMillis = reminderDate ?: System.currentTimeMillis(),
            selectableDates = dateValidator
        )
        DatePickerDialog(
            onDismissRequest = { },
            confirmButton = {
                TextButton(
                    onClick = {
                        pendingReminderDate = reminderPickerState.selectedDateMillis
                        if (pendingReminderDate != null) showTimePicker = true
                    }
                ) { Text(stringResource(R.string.next)) }
            },
            dismissButton = {
                TextButton(onClick = { }) {
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
                pendingReminderDate = null
            }
        )
    }
}



