package com.example.taskwhiz.presentation.ui.taskEditorScreen.components

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect

@Composable
fun NotificationPermissionHandler(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) onPermissionGranted() else onPermissionDenied()
        }

        // Trigger this when the user clicks "Set Reminder"
        SideEffect {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}