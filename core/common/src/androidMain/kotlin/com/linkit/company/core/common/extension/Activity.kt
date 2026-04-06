package com.linkit.company.core.common.extension

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

fun ComponentActivity.enableEdgeToEdgeConfig() {
    enableEdgeToEdge()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        window.isNavigationBarContrastEnforced = false
    }
}
