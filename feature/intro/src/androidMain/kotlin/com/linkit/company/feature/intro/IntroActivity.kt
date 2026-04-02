package com.linkit.company.feature.intro

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.linkit.company.core.common.extension.enableEdgeToEdgeConfig
import com.linkit.company.core.designsystem.theme.LinkItTheme

class IntroActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdgeConfig()
        super.onCreate(savedInstanceState)

        setContent {
            LinkItTheme {
                IntroScreen(
                    onNavigateToHome = {
                        startActivity(
                            Intent().setClassName(
                                packageName,
                                "com.linkit.company.feature.home.HomeActivity",
                            )
                        )
                        finish()
                    },
                )
            }
        }
    }
}
