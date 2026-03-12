package com.linkit.company

import androidx.compose.ui.window.ComposeUIViewController
import com.linkit.company.AppGraph

@Suppress("UNUSED")
fun MainViewController(appGraph: AppGraph) = ComposeUIViewController {
    with(appGraph) {
        LinkItApp()
    }
}
