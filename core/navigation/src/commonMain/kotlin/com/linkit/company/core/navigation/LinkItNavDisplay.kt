package com.linkit.company.core.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.defaultPopTransitionSpec
import androidx.navigation3.ui.defaultPredictivePopTransitionSpec
import androidx.navigation3.ui.defaultTransitionSpec
import androidx.navigationevent.NavigationEvent

val LocalLinkItNavigator = compositionLocalOf<LinkItNavigator> {
    error("LocalLinkItNavigator not provided")
}

@Composable
fun LinkItNavDisplay(
    backStack: NavBackStack<NavKey>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    entryDecorators: List<NavEntryDecorator<NavKey>> = emptyList(),
    contentAlignment: Alignment = Alignment.TopCenter,
    sceneStrategy: SceneStrategy<NavKey> = SinglePaneSceneStrategy(),
    sizeTransform: SizeTransform? = null,
    transitionSpec: (AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform)? = null,
    popTransitionSpec: (AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform)? = null,
    predictivePopTransitionSpec: (AnimatedContentTransitionScope<Scene<NavKey>>.(@NavigationEvent.SwipeEdge Int) -> ContentTransform)? = null,
    entryProvider: (key: NavKey) -> NavEntry<NavKey>,
) {
    val transitionSpec = transitionSpec ?: defaultTransitionSpec()
    val popTransitionSpec = popTransitionSpec ?: defaultPopTransitionSpec()
    val predictivePopTransitionSpec =
        predictivePopTransitionSpec ?: defaultPredictivePopTransitionSpec()

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = onBack,
        entryDecorators = entryDecorators,
        contentAlignment = contentAlignment,
        sceneStrategy = sceneStrategy,
        sizeTransform = sizeTransform,
        transitionSpec = transitionSpec,
        popTransitionSpec = popTransitionSpec,
        predictivePopTransitionSpec = predictivePopTransitionSpec,
        entryProvider = entryProvider,
    )
}

@Composable
fun LinkItNavDisplay(
    entries: List<NavEntry<NavKey>>,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopCenter,
    sceneStrategy: SceneStrategy<NavKey> = SinglePaneSceneStrategy(),
    sizeTransform: SizeTransform? = null,
    transitionSpec: (AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform)? = null,
    popTransitionSpec: (AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform)? = null,
    predictivePopTransitionSpec: (AnimatedContentTransitionScope<Scene<NavKey>>.(@NavigationEvent.SwipeEdge Int) -> ContentTransform)? = null,
    onBack: () -> Unit,
) {
    val transitionSpec = transitionSpec ?: defaultTransitionSpec()
    val popTransitionSpec = popTransitionSpec ?: defaultPopTransitionSpec()
    val predictivePopTransitionSpec =
        predictivePopTransitionSpec ?: defaultPredictivePopTransitionSpec()

    NavDisplay(
        entries = entries,
        modifier = modifier,
        contentAlignment = contentAlignment,
        sceneStrategy = sceneStrategy,
        sizeTransform = sizeTransform,
        transitionSpec = transitionSpec,
        popTransitionSpec = popTransitionSpec,
        predictivePopTransitionSpec = predictivePopTransitionSpec,
        onBack = onBack,
    )
}