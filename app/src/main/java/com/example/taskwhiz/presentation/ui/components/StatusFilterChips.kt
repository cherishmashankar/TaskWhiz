package com.example.taskwhiz.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskwhiz.R
import com.example.taskwhiz.presentation.ui.theme.AppDimens

@Composable
fun StatusFilterChips(
    selectedStatus: String,
    onStatusSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val statusFilters = listOf(
        stringResource(id = R.string.filter_all),
        stringResource(id = R.string.filter_completed),
        stringResource(id = R.string.filter_pending)
    )

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppDimens.PaddingLarge, vertical = AppDimens.PaddingXSmall),
        horizontalArrangement = Arrangement.spacedBy(AppDimens.PaddingSmall)
    ) {
        items(statusFilters.size) { i ->
            val filter = statusFilters[i]
            val isSelected = selectedStatus == filter

            FilterChip(
                selected = isSelected,
                onClick = { onStatusSelected(filter) },
                label = {
                    Text(
                        text = filter,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                    containerColor = Color.Transparent
                ),
                border = if (isSelected) null else BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            )
        }
    }
}
