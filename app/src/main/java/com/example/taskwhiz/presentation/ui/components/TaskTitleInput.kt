package com.example.taskwhiz.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.core.graphics.toColorInt

@Composable
fun TaskTitleInput(
    title: String,
    onTitleChange: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    BasicTextField(
        value = title,
        onValueChange = { newText ->
            // Restrict to one line (ignore newlines)
            onTitleChange(newText.replace("\n", ""))
        },
        textStyle = TextStyle(
            fontSize = 24.sp,       // Bigger, more focus
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                // Clear focus when Enter is pressed
                focusManager.clearFocus()
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),  // Spacing instead of line
        decorationBox = { innerTextField ->
            if (title.isEmpty()) {
                Text(
                    "Task Title",
                    color = Color.Gray,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            innerTextField()
        }
    )
}

