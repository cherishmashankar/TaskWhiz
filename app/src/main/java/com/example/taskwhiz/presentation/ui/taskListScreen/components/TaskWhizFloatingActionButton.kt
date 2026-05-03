package com.example.taskwhiz.presentation.ui.taskListScreen.components


import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.taskwhiz.presentation.ui.theme.AppDimens
import com.example.taskwhiz.R


@Composable
fun TaskWhizFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_task_add),
            contentDescription = stringResource(id = R.string.add_task),
            modifier = Modifier.size(AppDimens.IconLarge + AppDimens.PaddingSmall)
        )
    }
}