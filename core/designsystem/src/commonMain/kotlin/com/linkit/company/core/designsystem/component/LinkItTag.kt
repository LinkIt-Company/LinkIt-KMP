package com.linkit.company.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import com.linkit.company.core.designsystem.theme.LinkItShape
import com.linkit.company.core.designsystem.theme.LinkItTextStyle
import com.linkit.company.core.designsystem.theme.PrimaryBlue
import com.linkit.company.core.designsystem.theme.PrimaryBlueLight
import com.linkit.company.core.designsystem.theme.Slate50
import com.linkit.company.core.designsystem.theme.Slate500
import com.linkit.company.core.designsystem.theme.TagGreenBg
import com.linkit.company.core.designsystem.theme.TagGreenText
import com.linkit.company.core.designsystem.theme.TagRedBg
import com.linkit.company.core.designsystem.theme.TagRedText

enum class TagColorScheme(
    val backgroundColor: Color,
    val textColor: Color,
) {
    Blue(PrimaryBlueLight, PrimaryBlue),
    Green(TagGreenBg, TagGreenText),
    Red(TagRedBg, TagRedText),
    Slate(Slate50, Slate500),
}

data class TagData(
    val text: String,
    val colorScheme: TagColorScheme,
)

@Composable
fun LinkItTag(
    text: String,
    colorScheme: TagColorScheme,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LinkItTextStyle.tagText,
) {
    Box(
        modifier = modifier
            .background(
                color = colorScheme.backgroundColor,
                shape = LinkItShape.tag,
            )
            .padding(horizontal = 6.dp, vertical = 2.dp),
    ) {
        Text(
            text = text,
            style = textStyle,
            color = colorScheme.textColor,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LinkItTagRow(
    tags: List<TagData>,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        tags.forEach { tag ->
            LinkItTag(
                text = tag.text,
                colorScheme = tag.colorScheme,
            )
        }
    }
}
