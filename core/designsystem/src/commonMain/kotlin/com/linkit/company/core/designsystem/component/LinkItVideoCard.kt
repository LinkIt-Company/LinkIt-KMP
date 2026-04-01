package com.linkit.company.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linkit.company.core.designsystem.theme.Black
import com.linkit.company.core.designsystem.theme.LinkItFontFamily
import com.linkit.company.core.designsystem.theme.LinkItShape
import com.linkit.company.core.designsystem.theme.LinkItTextStyle
import com.linkit.company.core.designsystem.theme.White
import com.linkit.company.core.designsystem.theme.G2
import com.linkit.company.core.designsystem.theme.LinkItTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LinkItVideoCard(
    thumbnail: @Composable () -> Unit,
    title: String,
    viewCount: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onCopyLinkClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .width(160.dp)
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box {
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(90.dp)
                    .clip(LinkItShape.input),
            ) {
                thumbnail()
            }
            if (onCopyLinkClick != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 8.dp, bottom = 8.dp)
                        .background(
                            color = Color(0x66000000),
                            shape = LinkItShape.tag,
                        )
                        .clickable(onClick = onCopyLinkClick)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                ) {
                    Text(
                        text = "링크복사",
                        style = LinkItTextStyle.caption1.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        color = White,
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                text = title,
                style = LinkItTextStyle.sectionTitle.copy(
                    fontWeight = FontWeight.Medium,
                ),
                color = Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = viewCount,
                style = LinkItTextStyle.small,
                color = Color(0xB3000000),
            )
        }
    }
}

@Preview
@Composable
private fun LinkItVideoCardPreview() {
    LinkItTheme {
        LinkItVideoCard(
            thumbnail = {
                Box(
                    modifier = Modifier
                        .width(160.dp)
                        .height(90.dp)
                        .background(G2),
                )
            },
            title = "유부남과 함께 오사카 좋은 놀이공원 가보기",
            viewCount = "조회수 113만회",
            onClick = {},
            onCopyLinkClick = {},
        )
    }
}

@Preview
@Composable
private fun LinkItVideoCardNoCopyPreview() {
    LinkItTheme {
        LinkItVideoCard(
            thumbnail = {
                Box(
                    modifier = Modifier
                        .width(160.dp)
                        .height(90.dp)
                        .background(G2),
                )
            },
            title = "유부남과 함께 오사카 좋은 놀이공원 가보기",
            viewCount = "조회수 113만회",
            onClick = {},
        )
    }
}
