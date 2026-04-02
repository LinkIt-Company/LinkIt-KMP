package com.linkit.company

import androidx.compose.runtime.Composable
import com.linkit.company.core.designsystem.theme.LinkItTheme
import com.linkit.company.core.navigation.LinkItSavedStateConfiguration
import com.linkit.company.feature.home.HomeScreen

@Composable
context(appGraph: AppGraph)
fun LinkItApp() {
    LinkItTheme {
        HomeScreen(savedStateConfiguration = LinkItSavedStateConfiguration)
    }
}
