package com.linkit.company.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.linkit.company.core.designsystem.icon.LinkItIcons
import com.linkit.company.core.designsystem.theme.Black
import com.linkit.company.core.designsystem.theme.BorderDefault
import com.linkit.company.core.designsystem.theme.LinkItShape
import com.linkit.company.core.designsystem.theme.LinkItTextStyle
import com.linkit.company.core.designsystem.theme.PrimaryBlue
import com.linkit.company.core.designsystem.theme.PrimaryBlueBackground
import com.linkit.company.core.designsystem.theme.PrimaryBlueBorder
import com.linkit.company.core.designsystem.theme.White
import com.linkit.company.core.designsystem.theme.LinkItTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LinkItFilterChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val backgroundColor = if (selected) PrimaryBlueBackground else White
    val borderColor = if (selected) PrimaryBlueBorder else BorderDefault
    val textColor = if (selected) PrimaryBlue else Black

    Row(
        modifier = modifier
            .height(34.dp)
            .clip(LinkItShape.pill)
            .background(backgroundColor)
            .border(1.dp, borderColor, LinkItShape.pill)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        leadingIcon?.invoke()
        Text(
            text = label,
            style = LinkItTextStyle.caption1,
            color = textColor,
        )
        Icon(
            imageVector = LinkItIcons.ChevronDown,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = textColor,
        )
    }
}

@Preview
@Composable
private fun LinkItFilterChipDefaultPreview() {
    LinkItTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            LinkItFilterChip(label = "지역", onClick = {})
            LinkItFilterChip(label = "여행 테마", onClick = {})
            LinkItFilterChip(label = "비용", onClick = {})
        }
    }
}

@Preview
@Composable
private fun LinkItFilterChipSelectedPreview() {
    LinkItTheme {
        LinkItFilterChip(label = "지역", onClick = {}, selected = true)
    }
}
