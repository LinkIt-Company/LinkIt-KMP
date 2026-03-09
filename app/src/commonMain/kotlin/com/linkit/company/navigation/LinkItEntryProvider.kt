package com.linkit.company.navigation

import androidx.navigation3.runtime.entryProvider
import com.linkit.company.core.navigation.LinkItRoute
import com.linkit.company.core.navigation.LocalLinkItNavigator
import com.linkit.company.feature.explore.ExploreScreen
import com.linkit.company.feature.home.HomeScreen
import com.linkit.company.feature.intro.IntroScreen
import com.linkit.company.feature.schedule.ScheduleScreen
import com.linkit.company.feature.storage.StorageScreen

val linkItEntryProvider = entryProvider<LinkItRoute> {
    entry(LinkItRoute.Home) { _ ->
        val navigator = LocalLinkItNavigator.current
        HomeScreen(
            onNavigateToIntro = { navigator.navigate(LinkItRoute.Intro) },
            onNavigateToSchedule = { navigator.navigate(LinkItRoute.Schedule) },
            onNavigateToStorage = { navigator.navigate(LinkItRoute.Storage) },
            onNavigateToExplore = { navigator.navigate(LinkItRoute.Explore) },
        )
    }

    entry(LinkItRoute.Intro) { _ ->
        val navigator = LocalLinkItNavigator.current
        IntroScreen(
            onNavigateToHome = { navigator.navigateAndClearStack(LinkItRoute.Home) },
            onBack = { navigator.popBack() },
        )
    }

    entry(LinkItRoute.Schedule) { _ ->
        val navigator = LocalLinkItNavigator.current
        ScheduleScreen(
            onBack = { navigator.popBack() },
        )
    }

    entry(LinkItRoute.Storage) { _ ->
        val navigator = LocalLinkItNavigator.current
        StorageScreen(
            onBack = { navigator.popBack() },
        )
    }

    entry(LinkItRoute.Explore) { _ ->
        val navigator = LocalLinkItNavigator.current
        ExploreScreen(
            onBack = { navigator.popBack() },
        )
    }
}
