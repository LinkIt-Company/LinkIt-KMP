package com.linkit.company.feature.home.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.linkit.company.core.navigation.LinkItNavKey

val TopLevelRoutes = mapOf(
    LinkItNavKey.Map to TopLevelTab("지도", Icons.Filled.Home),
    LinkItNavKey.Storage to TopLevelTab("보관함", Icons.Filled.Home),
    LinkItNavKey.Explore to TopLevelTab("탐색", Icons.Filled.Search),
)

data class TopLevelTab(
    val label: String,
    val icon: ImageVector,
)
