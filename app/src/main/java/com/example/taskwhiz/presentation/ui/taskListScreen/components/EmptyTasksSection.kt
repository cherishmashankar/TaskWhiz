package com.example.taskwhiz.presentation.ui.taskListScreen.components


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.taskwhiz.presentation.ui.theme.AppDimens
import com.example.taskwhiz.R


@Composable
fun EmptyTasksScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimens.PaddingXLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                imageVector = Icons.Default.Inbox,
                contentDescription = null,
                modifier = Modifier
                    .size(AppDimens.IconXLarge * 2)
                    .padding(bottom = AppDimens.PaddingMedium),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f)
            )

            Text(
                text = stringResource(R.string.no_tasks),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(AppDimens.PaddingSmall))

            Text(
                text = stringResource(R.string.no_tasks_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = AppDimens.PaddingLarge)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyTasksScreenPreview() {
    EmptyTasksScreen()
}