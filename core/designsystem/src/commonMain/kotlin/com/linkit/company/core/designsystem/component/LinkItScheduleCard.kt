package com.linkit.company.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.linkit.company.core.designsystem.icon.LinkItIcons
import com.linkit.company.core.designsystem.theme.CardBackground
import com.linkit.company.core.designsystem.theme.CardBorder
import com.linkit.company.core.designsystem.theme.HeadingBlack
import com.linkit.company.core.designsystem.theme.LinkItShape
import com.linkit.company.core.designsystem.theme.LinkItTextStyle
import com.linkit.company.core.designsystem.theme.PrimaryBlue
import com.linkit.company.core.designsystem.theme.Slate500
import com.linkit.company.core.designsystem.theme.Slate600
import com.linkit.company.core.designsystem.theme.G2
import com.linkit.company.core.designsystem.theme.LinkItTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LinkItScheduleCard(
    image: @Composable () -> Unit,
    tags: List<TagData>,
    title: String,
    address: String,
    description: String,
    onDetailClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(1.dp, LinkItShape.scheduleCard)
            .background(CardBackground, LinkItShape.scheduleCard)
            .border(1.dp, CardBorder, LinkItShape.scheduleCard)
            .padding(17.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Header: image + tags + title + address
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(LinkItShape.card),
            ) {
                image()
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                if (tags.isNotEmpty()) {
                    LinkItTagRow(tags = tags)
                }
                Text(
                    text = title,
                    style = LinkItTextStyle.heading,
                    color = HeadingBlack,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = address,
                        style = LinkItTextStyle.small,
                        color = Slate500,
                    )
                }
            }
        }

        // Description
        if (description.isNotEmpty()) {
            Text(
                text = description,
                style = LinkItTextStyle.body,
                color = Slate600,
            )
        }

        // Detail button
        Row(
            modifier = Modifier.clickable(onClick = onDetailClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "자세히 보기",
                style = LinkItTextStyle.caption1.copy(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                ),
                color = PrimaryBlue,
            )
            Icon(
                imageVector = LinkItIcons.ChevronRight,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = PrimaryBlue,
            )
        }
    }
}

@Preview
@Composable
private fun LinkItScheduleCardPreview() {
    LinkItTheme {
        LinkItScheduleCard(
            image = {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(G2),
                )
            },
            tags = listOf(
                TagData("박물관", TagColorScheme.Blue),
                TagData("조용한", TagColorScheme.Green),
            ),
            title = "루브르 박물관",
            address = "뤼 드 리볼리, 75001 파리",
            description = "세계 각지의 유물이 모여있는 박물관 입니다. 모나리자를 위해 드농 윙에 집중하세요.",
            onDetailClick = {},
        )
    }
}
