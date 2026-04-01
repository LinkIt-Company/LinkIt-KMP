package com.linkit.company.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.linkit.company.core.designsystem.icon.LinkItIcons
import com.linkit.company.core.designsystem.theme.HeaderText
import com.linkit.company.core.designsystem.theme.LinkItTextStyle
import com.linkit.company.core.designsystem.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkItTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    Column(modifier = modifier) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    style = LinkItTextStyle.heading,
                    color = HeaderText,
                )
            },
            navigationIcon = {
                if (showBackButton) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = LinkItIcons.ArrowBack,
                            contentDescription = "뒤로 가기",
                        )
                    }
                }
            },
            actions = actions,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = White,
                navigationIconContentColor = HeaderText,
                actionIconContentColor = HeaderText,
            ),
        )
        LinkItDivider()
    }
}