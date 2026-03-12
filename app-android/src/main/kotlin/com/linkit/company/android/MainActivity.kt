package com.linkit.company.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.linkit.company.LinkItApp
import com.linkit.company.extension.enableEdgeToEdgeConfig

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdgeConfig()
        super.onCreate(savedInstanceState)

        with(appGraph) {
            setContent {
                LinkItApp()
            }
        }
    }
}
