package com.linkit.company

import androidx.compose.runtime.Composable
import com.linkit.company.core.designsystem.theme.LinkItTheme
import com.linkit.company.core.navigation.LinkItNavDisplay
import com.linkit.company.navigation.linkItEntryProvider

@Composable
context(appGraph: AppGraph)
fun LinkItApp() {
    LinkItTheme {
        LinkItNavDisplay(
            entryProvider = linkItEntryProvider,
        )
    }
}
