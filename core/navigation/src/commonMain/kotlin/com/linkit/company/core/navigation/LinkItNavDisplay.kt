package com.linkit.company.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer

val LocalLinkItNavigator = compositionLocalOf<LinkItNavigator> {
    error("LocalLinkItNavigator not provided")
}

private val startDestination = LinkItRoute.Home

@Composable
fun LinkItNavDisplay(
    entryProvider: (LinkItRoute) -> NavEntry<LinkItRoute>,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    entryDecorators: List<NavEntryDecorator<LinkItRoute>> =
        listOf(rememberSaveableStateHolderNavEntryDecorator()),
    sceneStrategy: SceneStrategy<LinkItRoute> = SinglePaneSceneStrategy(),
) {
    val backStack = rememberSerializable(
        serializer = SnapshotStateListSerializer(LinkItRoute.serializer()),
    ) {
        listOf(startDestination).toMutableStateList()
    }
    val navigator = remember(backStack) { LinkItNavigator(backStack) }

    CompositionLocalProvider(LocalLinkItNavigator provides navigator) {
        NavDisplay(
            backStack = backStack,
            modifier = modifier,
            contentAlignment = contentAlignment,
            onBack = { navigator.popBack() },
            entryDecorators = entryDecorators,
            sceneStrategy = sceneStrategy,
            entryProvider = entryProvider,
        )
    }
}
