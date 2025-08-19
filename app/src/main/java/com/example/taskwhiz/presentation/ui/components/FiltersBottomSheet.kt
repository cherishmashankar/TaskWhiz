package com.example.taskwhiz.presentation.ui.components

/*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersBottomSheet(
    onDismiss: () -> Unit,
    sort: SortOption,
    onSortChange: (SortOption) -> Unit,
    language: LanguageOption,
    onLanguageChange: (LanguageOption) -> Unit,
    theme: ThemeOption,
    onThemeChange: (ThemeOption) -> Unit,
    due: DueFilter,
    onDueChange: (DueFilter) -> Unit,
    hasReminderOnly: Boolean,
    onHasReminderOnlyChange: (Boolean) -> Unit,
    highPriorityOnly: Boolean,
    onHighPriorityOnlyChange: (Boolean) -> Unit,
    onReset: () -> Unit,
    onApply: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .navigationBarsPadding()
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            Text("Filters & Settings", style = MaterialTheme.typography.titleLarge)

            // Sort
            SectionHeader("Sort by")
            SingleChoiceSegmentedButtonRow {
                Segment(SortOption.None, sort, onSortChange, "None")
                Segment(SortOption.Date, sort, onSortChange, "Date")
                Segment(SortOption.Color, sort, onSortChange, "Color")
            }

            // Language
            SectionHeader("Language")
            SingleChoiceSegmentedButtonRow {
                Segment(LanguageOption.EN, language, onLanguageChange, "EN")
                Segment(LanguageOption.DE, language, onLanguageChange, "DE")
            }

            // Theme
            SectionHeader("Color mode")
            SingleChoiceSegmentedButtonRow {
                Segment(ThemeOption.System, theme, onThemeChange, "System")
                Segment(ThemeOption.Light, theme, onThemeChange, "Light")
                Segment(ThemeOption.Dark, theme, onThemeChange, "Dark")
            }

            // Due window
            SectionHeader("Due")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DueChip("Any", DueFilter.Any, due, onDueChange)
                DueChip("Today", DueFilter.Today, due, onDueChange, Icons.Default.Today)
                DueChip("This week", DueFilter.ThisWeek, due, onDueChange, Icons.Default.DateRange)
                DueChip("This month", DueFilter.ThisMonth, due, onDueChange, Icons.Default.CalendarMonth)
                DueChip("Overdue", DueFilter.Overdue, due, onDueChange, Icons.Default.Warning)
            }

            // Toggles
            SectionHeader("More")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Has reminder", style = MaterialTheme.typography.bodyMedium)
                }
                Switch(checked = hasReminderOnly, onCheckedChange = onHasReminderOnlyChange)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color(0xFFFFB300))
                    Spacer(Modifier.width(8.dp))
                    Text("High priority only", style = MaterialTheme.typography.bodyMedium)
                }
                Switch(checked = highPriorityOnly, onCheckedChange = onHighPriorityOnlyChange)
            }

            Divider(modifier = Modifier.padding(top = 4.dp))

            // Actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onReset,
                    modifier = Modifier.weight(1f)
                ) { Text("Reset") }

                Button(
                    onClick = {
                        onApply()
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Apply") }
            }
        }
    }
}

*/
/* --- Small helpers --- *//*


@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> SingleChoiceSegmentedButtonRow.Scope.Segment(
    value: T,
    selected: T,
    onSelected: (T) -> Unit,
    label: String
) {
    SegmentedButton(
        selected = value == selected,
        onClick = { onSelected(value) },
        shape = SegmentedButtonDefaults.itemShape(index = null, count = null),
        label = { Text(label) }
    )
}

@Composable
private fun DueChip(
    text: String,
    value: DueFilter,
    selected: DueFilter,
    onSelected: (DueFilter) -> Unit,
    leading: ImageVector? = null
) {
    FilterChip(
        selected = value == selected,
        onClick = { onSelected(value) },
        label = { Text(text) },
        leadingIcon = leading?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    )
}

*/
/* --- Models --- *//*

enum class SortOption { None, Date, Color }
enum class LanguageOption { EN, DE }
enum class ThemeOption { System, Light, Dark }
enum class DueFilter { Any, Today, ThisWeek, ThisMonth, Overdue }
*/
