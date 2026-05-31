package com.example.taskwhiz.presentation.ui.taskCaptureScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskwhiz.domain.model.Task
import com.example.taskwhiz.presentation.ui.taskCaptureScreen.componets.TaskCaptureContent
import com.example.taskwhiz.presentation.ui.taskCaptureScreen.componets.TaskCaptureHeader
import com.example.taskwhiz.presentation.ui.taskCaptureScreen.componets.TaskInputSection
import com.example.taskwhiz.presentation.ui.taskEditorScreen.TaskEditorViewModel
import com.example.taskwhiz.presentation.ui.theme.AppDimens


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCaptureScreen(
    onDismiss: () -> Unit,
    taskCaptureViewModel: TaskCaptureViewModel = hiltViewModel()
) {

    val uiState by taskCaptureViewModel.uiState.collectAsState()

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            TaskCaptureHeader()

            Spacer(modifier = Modifier.height(AppDimens.PaddingXLarge))

            TaskInputSection(
                input = "", // UI controlled (temporary)
                onSubmit = { text ->
                    taskCaptureViewModel.onEvent(
                        TaskCaptureEvent.OnSubmit(text)
                    )
                }
            )

            Spacer(modifier = Modifier.height(AppDimens.PaddingXLarge))

            TaskCaptureContent(
                uiState = uiState,
                input = "",
                onEvent = { event ->
                    taskCaptureViewModel.onEvent(event)
                }
            )

            Spacer(modifier = Modifier.height(AppDimens.PaddingXLarge))
        }
    }
}



