package com.example.taskwhiz.presentation.ui.taskCaptureScreen

sealed interface TaskCaptureEvent {

    data class OnSubmit(val text: String) : TaskCaptureEvent

    data object OnSave : TaskCaptureEvent

    data object OnEdit : TaskCaptureEvent

    data object Reset : TaskCaptureEvent
}