package com.linkit.company.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.savedstate.serialization.SavedStateConfiguration

@Composable
fun rememberNavigationState(
    savedStateConfiguration: SavedStateConfiguration,
    startRoute: NavKey,
    topLevelRoutes: Set<NavKey>,
): NavigationState {
    val topLevelStack = rememberNavBackStack(savedStateConfiguration, startRoute)
    val topLevelBackStacks = topLevelRoutes.associateWith { key ->
        rememberNavBackStack(savedStateConfiguration, key)
    }

    return remember(key1 = startRoute, key2 = topLevelRoutes) {
        NavigationState(
            startRoute = startRoute,
            topLevelStack = topLevelStack,
            backStacks = topLevelBackStacks,
        )
    }
}

class NavigationState(
    val startRoute: NavKey,
    val topLevelStack: NavBackStack<NavKey>,
    val backStacks: Map<NavKey, NavBackStack<NavKey>>,
) {
    val currentTopLevelRoute: NavKey by derivedStateOf { topLevelStack.last() }
    val currentRoute: NavKey by derivedStateOf { currentTopLevelBackStack.last() }

    val topLevelRoutes: Set<NavKey>
        get() = backStacks.keys

    val currentTopLevelBackStack: NavBackStack<NavKey>
        get() = backStacks[currentTopLevelRoute]
            ?: error("Back stack for $currentTopLevelRoute does not exist")
}

@Composable
fun NavigationState.toDecoratedEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>,
): SnapshotStateList<NavEntry<NavKey>> {
    val decoratedEntries = backStacks.mapValues { (_, stack) ->
        val decorators: List<NavEntryDecorator<NavKey>> = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        )

        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators = decorators,
            entryProvider = entryProvider,
        )
    }

    return topLevelStack
        .flatMap { decoratedEntries[it] ?: emptyList() }
        .toMutableStateList()
}
