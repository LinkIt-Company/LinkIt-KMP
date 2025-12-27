package com.linkit.company.core.navigation

import androidx.navigation3.runtime.entryProvider
import com.linkit.company.feature.classification.ClassificationScreen
import com.linkit.company.feature.home.HomeScreen
import com.linkit.company.feature.onboarding.OnboardingScreen
import com.linkit.company.feature.save.SaveScreen
import com.linkit.company.feature.share.ShareScreen
import com.linkit.company.feature.storage.StorageScreen

internal val linkItEntryProvider = entryProvider {
    entry(LinkItRoute.Home) { _ ->
        HomeScreen()
    }

    entry(LinkItRoute.Onboarding) { _ ->
        OnboardingScreen()
    }

    entry(LinkItRoute.Save) { _ ->
        SaveScreen()
    }

    entry(LinkItRoute.Share) { _ ->
        ShareScreen()
    }

    entry(LinkItRoute.Storage) { _ ->
        StorageScreen()
    }

    entry(LinkItRoute.Classification) { _ ->
        ClassificationScreen()
    }
}
