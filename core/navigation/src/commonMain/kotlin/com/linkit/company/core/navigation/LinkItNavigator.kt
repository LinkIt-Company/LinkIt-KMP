package com.linkit.company.core.navigation

import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.snapshots.SnapshotStateList

class LinkItNavigator internal constructor(
    private val backStack: SnapshotStateList<LinkItRoute>,
) {
    fun navigate(route: LinkItRoute) {
        if (backStack.lastOrNull() != route) {
            backStack.add(route)
        }
    }

    fun popBack() {
        if (backStack.size > 1) {
            backStack.removeLast()
        }
    }

    fun navigateAndClearStack(route: LinkItRoute) {
        Snapshot.withMutableSnapshot {
            backStack.clear()
            backStack.add(route)
        }
    }
}
