package com.example.taskwhiz.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.DialogProperties

@Composable
fun RawInputDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    if (showDialog) {
        var text by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (text.isNotBlank()) {
                            onSubmit(text.trim())
                            onDismiss()
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text("Cancel")
                }
            },
            title = {
                Text(
                    text = "New Task Note",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column {
                    Text("Enter your rough task note below:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholder = { Text("e.g. Remind me to call Alex next Tuesday") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 5,
                        textStyle = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 6.dp
        )
    }
}
