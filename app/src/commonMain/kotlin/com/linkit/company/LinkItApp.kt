package com.linkit.company

import androidx.compose.runtime.Composable
import com.linkit.company.core.designsystem.theme.LinkItTheme
import com.linkit.company.core.navigation.LinkItNavDisplay
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun LinkItApp() {
    LinkItTheme {
        LinkItNavDisplay()
    }
}
