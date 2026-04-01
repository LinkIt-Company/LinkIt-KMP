package com.linkit.company.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface LinkItRoute {
    @Serializable
    data object Home : LinkItRoute

    @Serializable
    data object ScheduleEdit : LinkItRoute

    @Serializable
    data object DesignShowcase : LinkItRoute
}
