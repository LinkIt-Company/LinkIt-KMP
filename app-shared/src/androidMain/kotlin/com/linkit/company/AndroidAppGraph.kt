package com.linkit.company

import android.content.Context
import com.linkit.company.data.DataScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides

@DependencyGraph(
    scope = AppScope::class,
    additionalScopes = [DataScope::class],
    // isExtendable = true
)
interface AndroidAppGraph : AppGraph {

    @DependencyGraph.Factory
    fun interface Factory {
        fun createAndroidAppGraph(
            @Provides applicationContext: Context,
        ) : AndroidAppGraph
    }
}