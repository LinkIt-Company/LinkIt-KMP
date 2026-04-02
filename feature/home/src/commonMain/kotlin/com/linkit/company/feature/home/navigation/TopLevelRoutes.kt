package com.linkit.company.feature.home.navigation

import com.linkit.company.core.navigation.LinkItNavKey

val TopLevelRoutes = mapOf(
    LinkItNavKey.Map to NavigationBarItem("Map"),
    LinkItNavKey.Storage to NavigationBarItem("Storage"),
    LinkItNavKey.Explore to NavigationBarItem("Explore"),
)

data class NavigationBarItem(
    val label: String,
//    val icon: ImageVector,
)