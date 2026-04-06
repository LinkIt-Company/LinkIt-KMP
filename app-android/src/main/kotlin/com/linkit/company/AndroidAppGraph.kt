package com.linkit.company

import android.content.Context
import com.linkit.company.core.common.AppGraph
import com.linkit.company.data.DataScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.android.MetroAppComponentProviders

@DependencyGraph(
    scope = AppScope::class,
    additionalScopes = [DataScope::class],
    // isExtendable = true
)
interface AndroidAppGraph : AppGraph, MetroAppComponentProviders {

    @DependencyGraph.Factory
    fun interface Factory {
        fun createAndroidAppGraph(
            @Provides applicationContext: Context,
        ) : AndroidAppGraph
    }
}