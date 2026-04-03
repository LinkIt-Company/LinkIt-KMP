package com.linkit.company.feature.map.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.linkit.company.core.navigation.LinkItNavKey
import com.linkit.company.feature.map.MapScreen

fun EntryProviderScope<NavKey>.mapEntry(
    onOpenSchedule: () -> Unit,
    navigateToScheduleEdit: () -> Unit,
) {
    entry<LinkItNavKey.Map> {
        MapScreen(
            onOpenSchedule = onOpenSchedule,
            navigateToScheduleEdit = navigateToScheduleEdit,
        )
    }
}