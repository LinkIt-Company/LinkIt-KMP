package com.linkit.company.navigation

import androidx.navigation3.runtime.entryProvider
import com.linkit.company.core.navigation.LinkItRoute
import com.linkit.company.core.navigation.LocalLinkItNavigator
import com.linkit.company.feature.classification.ClassificationScreen
import com.linkit.company.feature.home.HomeScreen
import com.linkit.company.feature.onboarding.OnboardingScreen
import com.linkit.company.feature.save.SaveScreen
import com.linkit.company.feature.share.ShareScreen
import com.linkit.company.feature.storage.StorageScreen

val linkItEntryProvider = entryProvider<LinkItRoute> {
    entry(LinkItRoute.Home) { _ ->
        val navigator = LocalLinkItNavigator.current
        HomeScreen(
            onNavigateToOnboarding = { navigator.navigate(LinkItRoute.Onboarding) },
            onNavigateToSave = { navigator.navigate(LinkItRoute.Save) },
            onNavigateToShare = { navigator.navigate(LinkItRoute.Share) },
            onNavigateToStorage = { navigator.navigate(LinkItRoute.Storage) },
            onNavigateToClassification = { navigator.navigate(LinkItRoute.Classification) },
        )
    }

    entry(LinkItRoute.Onboarding) { _ ->
        val navigator = LocalLinkItNavigator.current
        OnboardingScreen(
            onNavigateToHome = { navigator.navigateAndClearStack(LinkItRoute.Home) },
            onBack = { navigator.popBack() },
        )
    }

    entry(LinkItRoute.Save) { _ ->
        val navigator = LocalLinkItNavigator.current
        SaveScreen(
            onBack = { navigator.popBack() },
        )
    }

    entry(LinkItRoute.Share) { _ ->
        val navigator = LocalLinkItNavigator.current
        ShareScreen(
            onBack = { navigator.popBack() },
        )
    }

    entry(LinkItRoute.Storage) { _ ->
        val navigator = LocalLinkItNavigator.current
        StorageScreen(
            onBack = { navigator.popBack() },
        )
    }

    entry(LinkItRoute.Classification) { _ ->
        val navigator = LocalLinkItNavigator.current
        ClassificationScreen(
            onBack = { navigator.popBack() },
        )
    }
}
