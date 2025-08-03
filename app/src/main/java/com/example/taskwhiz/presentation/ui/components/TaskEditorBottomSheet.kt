package com.example.taskwhiz.presentation.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true, //Expanded
        confirmValueChange = { true }
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.fillMaxSize()
    ) {

        var title by remember { mutableStateOf(task?.title ?: "") }
        var selectedColor by remember { mutableStateOf(task?.colorCode ?: "#FF6F61") }
        var subtasks by remember { mutableStateOf(task?.taskItems ?: listOf("")) }

        // Root layout: full height, with padding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 1) Fixed Title at top
            TaskTitleInput(
                title = title,
                onTitleChange = { title = it }
            )

            Spacer(Modifier.height(12.dp))

            // 2) Scrollable middle section
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
            }

            Spacer(Modifier.height(16.dp))


            Button(
                onClick = {
                    val taskToHandle = Task(
                        id = task?.id ?: 0L,
                        title = title,
                        colorCode = selectedColor,
                        taskItems = subtasks.filter { it.isNotBlank() },
                        createdAt = task?.createdAt ?: System.currentTimeMillis(),
                        isMessy = task?.isMessy ?: true,
                        priorityLevel = task?.priorityLevel ?: 0
                    )

                    if (task == null) {
                        onSave(taskToHandle)
                    } else {
                        onUpdate(taskToHandle)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(selectedColor.toColorInt())
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (task == null) "Save Task" else "Update Task",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}