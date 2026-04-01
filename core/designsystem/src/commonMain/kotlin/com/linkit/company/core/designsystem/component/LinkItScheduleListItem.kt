package com.linkit.company.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.linkit.company.core.designsystem.icon.LinkItIcons
import com.linkit.company.core.designsystem.theme.Black
import com.linkit.company.core.designsystem.theme.G6
import com.linkit.company.core.designsystem.theme.LinkItShape
import com.linkit.company.core.designsystem.theme.LinkItTextStyle
import com.linkit.company.core.designsystem.theme.Slate500
import com.linkit.company.core.designsystem.theme.G2
import com.linkit.company.core.designsystem.theme.LinkItTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background

@Composable
fun LinkItScheduleListItem(
    thumbnail: @Composable () -> Unit,
    tags: List<TagData>,
    title: String,
    duration: String,
    cost: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    summary: String? = null,
    onMoreClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(width = 80.dp, height = 100.dp)
                .clip(LinkItShape.input),
        ) {
            thumbnail()
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            // Tags + more button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LinkItTagRow(
                    tags = tags,
                    modifier = Modifier.weight(1f),
                )
                if (onMoreClick != null) {
                    IconButton(
                        onClick = onMoreClick,
                        modifier = Modifier.size(24.dp),
                    ) {
                        Icon(
                            imageVector = LinkItIcons.MoreVert,
                            contentDescription = "더보기",
                            modifier = Modifier.size(20.dp),
                            tint = G6,
                        )
                    }
                }
            }

            // Title
            Text(
                text = title,
                style = LinkItTextStyle.heading,
                color = Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            // Meta info (duration | cost)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = duration,
                    style = LinkItTextStyle.small,
                    color = Slate500,
                )
                Text(
                    text = cost,
                    style = LinkItTextStyle.small,
                    color = Slate500,
                )
            }

            // AI Summary
            if (!summary.isNullOrEmpty()) {
                Text(
                    text = summary,
                    style = LinkItTextStyle.small,
                    color = G6,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview
@Composable
private fun LinkItScheduleListItemPreview() {
    LinkItTheme {
        LinkItScheduleListItem(
            thumbnail = {
                Box(
                    modifier = Modifier
                        .size(width = 80.dp, height = 100.dp)
                        .background(G2),
                )
            },
            tags = listOf(
                TagData("#먹방여행", TagColorScheme.Slate),
                TagData("#SNS 핫플레이스", TagColorScheme.Slate),
            ),
            title = "도쿄 신주쿠 여행",
            duration = "3박4일",
            cost = "82만원",
            summary = "AI 한줄 요약된 여행지 정보",
            onClick = {},
            onMoreClick = {},
        )
    }
}
