package com.linkit.company.android

import android.app.Application
import android.content.Context
import com.linkit.company.AndroidAppGraph
import com.linkit.company.AppGraph
import dev.zacsweers.metro.createGraphFactory

class LinkitApplication : Application() {
    val appGraph: AppGraph by lazy {
        createGraphFactory<AndroidAppGraph.Factory>().createAndroidAppGraph(
            applicationContext = this,
        )
    }
}

val Context.appGraph: AppGraph get() = (applicationContext as LinkitApplication).appGraph