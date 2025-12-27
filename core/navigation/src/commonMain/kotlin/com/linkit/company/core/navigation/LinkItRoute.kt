package com.linkit.company.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface LinkItRoute {
    @Serializable
    data object Home : LinkItRoute

    @Serializable
    data object Onboarding : LinkItRoute

    @Serializable
    data object Save : LinkItRoute

    @Serializable
    data object Share : LinkItRoute

    @Serializable
    data object Storage : LinkItRoute

    @Serializable
    data object Classification : LinkItRoute
}
