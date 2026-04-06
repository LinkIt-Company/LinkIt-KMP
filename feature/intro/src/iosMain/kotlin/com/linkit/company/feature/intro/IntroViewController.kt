package com.linkit.company.feature.intro

import androidx.compose.ui.window.ComposeUIViewController
import com.linkit.company.core.designsystem.theme.LinkItTheme

fun IntroViewController(onComplete: () -> Unit) = ComposeUIViewController {
    LinkItTheme {
        IntroScreen(
            onNavigateToHome = onComplete,
        )
    }
}
