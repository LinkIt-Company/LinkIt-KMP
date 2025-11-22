package com.linkit.company

import androidx.compose.runtime.Composable
import com.linkit.company.core.designsystem.theme.LinkItTheme
import com.linkit.company.feature.home.HomeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun LinkItApp() {
    LinkItTheme {
        HomeScreen()
    }
}
