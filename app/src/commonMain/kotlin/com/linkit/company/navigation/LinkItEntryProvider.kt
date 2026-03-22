package com.linkit.company.navigation

import androidx.navigation3.runtime.entryProvider
import com.linkit.company.core.navigation.LinkItRoute
import com.linkit.company.core.navigation.LocalLinkItNavigator
import com.linkit.company.feature.explore.ExploreScreen
import com.linkit.company.feature.home.HomeScreen
import com.linkit.company.feature.map.MapScreen
import com.linkit.company.feature.schedule.ScheduleEditScreen
import com.linkit.company.feature.schedule.ScheduleScreen
import com.linkit.company.feature.storage.StorageScreen

val linkItEntryProvider = entryProvider<LinkItRoute> {
    entry(LinkItRoute.Home) { _ ->
        val navigator = LocalLinkItNavigator.current
        HomeScreen(
            mapContent = { onOpenSchedule ->
                MapScreen(onOpenSchedule = onOpenSchedule)
            },
            storageContent = { StorageScreen() },
            exploreContent = { ExploreScreen() },
            scheduleSheetContent = { onDismissSheet ->
                ScheduleScreen(
                    onNavigateToScheduleEdit = {
                        onDismissSheet()
                        navigator.navigate(LinkItRoute.ScheduleEdit)
                    },
                    onBack = onDismissSheet,
                )
            },
        )
    }

    entry(LinkItRoute.ScheduleEdit) { _ ->
        val navigator = LocalLinkItNavigator.current
        ScheduleEditScreen(
            onBack = { navigator.popBack() },
        )
    }
}
