package com.linkit.company.android

import android.app.Application
import com.linkit.company.AndroidAppGraph
import com.linkit.company.core.common.AppGraph
import dev.zacsweers.metro.createGraphFactory
import dev.zacsweers.metrox.android.MetroAppComponentProviders
import dev.zacsweers.metrox.android.MetroApplication

class LinkitApplication : Application(), MetroApplication {
    private val _appGraph: AppGraph by lazy {
        createGraphFactory<AndroidAppGraph.Factory>().createAndroidAppGraph(
            applicationContext = this,
        )
    }

    override val appComponentProviders: MetroAppComponentProviders
        get() = _appGraph as MetroAppComponentProviders
}
