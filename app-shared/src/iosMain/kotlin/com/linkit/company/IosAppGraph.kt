package com.linkit.company

import com.linkit.company.data.DataScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.createGraphFactory

/**
 * The iOS dependency graph cannot currently be resolved by the compiler plugin.
 * Therefore, we need to define the iOS dependency graph manually.
 * For more details, see: https://github.com/ZacSweers/metro/issues/460
 *
 * 컴파일러 플러그인 이슈로 인해 iOS에선 수동 주입 필요
 * see: https://github.com/DroidKaigi/conference-app-2025/blob/07b46e6585ea6bdafe8a52142d1dd456fddda387/app-shared/src/iosMain/kotlin/io/github/droidkaigi/confsched/IosAppGraph.kt#L93-L97
 */
@DependencyGraph(
    scope = AppScope::class,
    additionalScopes = [DataScope::class],
    // isExtendable = true
)
interface IosAppGraph : AppGraph {

    @DependencyGraph.Factory
    fun interface Factory {
        fun createIosAppGraph(
        ): IosAppGraph
    }
}

fun createIosAppGraph(): IosAppGraph {
    return createGraphFactory<IosAppGraph.Factory>().createIosAppGraph()
}