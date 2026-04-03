package com.linkit.company.feature.home

import androidx.compose.ui.window.ComposeUIViewController
import com.linkit.company.core.common.AppGraph
import com.linkit.company.core.designsystem.theme.LinkItTheme
import com.linkit.company.core.navigation.LinkItSavedStateConfiguration
import com.linkit.company.feature.home.navigation.HomeNavDisplay

@Suppress("UNUSED")
fun HomeViewController(appGraph: AppGraph) = ComposeUIViewController {
    LinkItTheme {
        HomeNavDisplay(savedStateConfiguration = LinkItSavedStateConfiguration)
    }
}
