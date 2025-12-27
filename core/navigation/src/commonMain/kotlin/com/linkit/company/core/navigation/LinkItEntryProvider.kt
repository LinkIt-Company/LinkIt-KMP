package com.linkit.company.core.navigation

import androidx.navigation3.runtime.entryProvider
import com.linkit.company.feature.classification.ClassificationScreen
import com.linkit.company.feature.home.HomeScreen
import com.linkit.company.feature.onboarding.OnboardingScreen
import com.linkit.company.feature.save.SaveScreen
import com.linkit.company.feature.share.ShareScreen
import com.linkit.company.feature.storage.StorageScreen

internal val linkItEntryProvider = entryProvider<LinkItRoute> {
    entry(LinkItRoute.Home) { _ ->
        val backStack = LocalLinkItBackStack.current
        HomeScreen(
            onNavigateToOnboarding = { backStack.add(LinkItRoute.Onboarding) },
            onNavigateToSave = { backStack.add(LinkItRoute.Save) },
            onNavigateToShare = { backStack.add(LinkItRoute.Share) },
            onNavigateToStorage = { backStack.add(LinkItRoute.Storage) },
            onNavigateToClassification = { backStack.add(LinkItRoute.Classification) },
        )
    }

    entry(LinkItRoute.Onboarding) { _ ->
        val backStack = LocalLinkItBackStack.current
        OnboardingScreen(
            onNavigateToHome = { 
                backStack.clear()
                backStack.add(LinkItRoute.Home)
            },
            onBack = { backStack.removeLastOrNull() },
        )
    }

    entry(LinkItRoute.Save) { _ ->
        val backStack = LocalLinkItBackStack.current
        SaveScreen(
            onBack = { backStack.removeLastOrNull() },
        )
    }

    entry(LinkItRoute.Share) { _ ->
        val backStack = LocalLinkItBackStack.current
        ShareScreen(
            onBack = { backStack.removeLastOrNull() },
        )
    }

    entry(LinkItRoute.Storage) { _ ->
        val backStack = LocalLinkItBackStack.current
        StorageScreen(
            onBack = { backStack.removeLastOrNull() },
        )
    }

    entry(LinkItRoute.Classification) { _ ->
        val backStack = LocalLinkItBackStack.current
        ClassificationScreen(
            onBack = { backStack.removeLastOrNull() },
        )
    }
}
