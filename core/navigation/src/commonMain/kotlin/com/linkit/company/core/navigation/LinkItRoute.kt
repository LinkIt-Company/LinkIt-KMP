package com.linkit.company.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface LinkItRoute {
    @Serializable
    data object Intro : LinkItRoute

    @Serializable
    data object Home : LinkItRoute

    @Serializable
    data object Schedule : LinkItRoute

    @Serializable
    data object Storage : LinkItRoute

    @Serializable
    data object Explore : LinkItRoute
}
