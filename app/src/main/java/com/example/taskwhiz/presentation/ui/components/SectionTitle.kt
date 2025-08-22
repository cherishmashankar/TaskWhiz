package com.example.taskwhiz.presentation.ui.components



import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.taskwhiz.presentation.ui.theme.AppDimens

@Composable
fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    fontSize: TextUnit = MaterialTheme.typography.titleLarge.fontSize
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge.copy(
            fontSize = fontSize,
            fontWeight = FontWeight.Bold   // 🔹 Bold font
        ),
        color = color,
        modifier = modifier
            .padding(
                start = AppDimens.PaddingLarge,
                bottom = AppDimens.PaddingSmall
            )
    )
}
