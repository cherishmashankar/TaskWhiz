package com.example.taskwhiz.presentation.ui.components


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskwhiz.presentation.ui.model.FilterItem
import com.example.taskwhiz.presentation.ui.theme.AppDimens

@Composable
fun FilterCard(
    item: FilterItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val disabled = item.count == 0
    val accent = item.color

    val container by animateColorAsState(
        targetValue =  MaterialTheme.colorScheme.surface ,
        label = "container"
    )
    val elevation by animateDpAsState(
        targetValue = if (isSelected) 6.dp else 2.dp,
        label = "elevation"
    )
    val border: BorderStroke? =
        if (isSelected) BorderStroke(1.5.dp, accent.copy(alpha = 0.65f)) else null

    Card(
        onClick = onClick,
        enabled = !disabled,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .then(if (disabled) Modifier.alpha(0.5f) else Modifier),
        shape = RoundedCornerShape(AppDimens.CornerMedium),
        colors = CardDefaults.cardColors(
            containerColor = container,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        border = border
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Row: icon (left) + count (right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.name,
                    tint =  accent,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = item.count.toString(),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = if (isSelected) accent else MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = item.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                ),
                color = if (isSelected) accent else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
