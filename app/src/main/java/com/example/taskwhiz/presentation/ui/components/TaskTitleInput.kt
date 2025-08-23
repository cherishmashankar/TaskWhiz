package com.example.taskwhiz.presentation.ui.components

import android.annotation.SuppressLint
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
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.core.graphics.toColorInt
import com.example.taskwhiz.R
import com.example.taskwhiz.presentation.ui.theme.AppDimens

@SuppressLint("SuspiciousIndentation")
@Composable
fun TaskTitleInput(
    title: String,
    onTitleChange: (String) -> Unit,
    modifier: Modifier,
    selectedColor: String,
) {
    val focusManager = LocalFocusManager.current

        BasicTextField(
            value = title,
            onValueChange = { newText ->
                onTitleChange(newText.replace("\n", ""))
            },
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
            textStyle = TextStyle(
                fontSize = 24.sp,       // Bigger, more focus
                fontWeight = FontWeight.Bold,
                color = Color(selectedColor.toColorInt())
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    // Clear focus when Enter is pressed
                    focusManager.clearFocus()
                }
            ),
            modifier =  modifier
                .fillMaxWidth()
                .padding(vertical = AppDimens.PaddingMedium),
            decorationBox = { innerTextField ->
                if (title.isEmpty()) {
                    Text(
                        text = stringResource(R.string.task_title_hint),
                        color = Color.Gray,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                innerTextField()
            }
        )
    }



