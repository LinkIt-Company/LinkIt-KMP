package com.linkit.company

import androidx.compose.ui.window.ComposeUIViewController
import com.linkit.company.core.designsystem.theme.LinkItTheme
import com.linkit.company.feature.intro.IntroScreen

fun IntroViewController(onComplete: () -> Unit) = ComposeUIViewController {
    LinkItTheme {
        IntroScreen(
            onNavigateToHome = onComplete,
        )
    }
}
