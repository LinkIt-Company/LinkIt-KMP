package com.linkit.company.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer

private val startDestination = LinkItRoute.Home

@Composable
fun rememberLinkItBackStack(): SnapshotStateList<LinkItRoute> {
    return rememberSerializable(serializer = SnapshotStateListSerializer<LinkItRoute>()) {
        listOf(startDestination).toMutableStateList()
    }
}

@Composable
fun LinkItNavDisplay(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    entryDecorators: List<NavEntryDecorator<LinkItRoute>> =
        listOf(rememberSaveableStateHolderNavEntryDecorator()),
    sceneStrategy: SceneStrategy<LinkItRoute> = SinglePaneSceneStrategy(),
) {
    val backStack = rememberLinkItBackStack()

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        contentAlignment = contentAlignment,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = entryDecorators,
        sceneStrategy = sceneStrategy,
        entryProvider = linkItEntryProvider,
    )
}
