package com.linkit.company.feature.intro

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.linkit.company.core.designsystem.theme.LinkItTheme

class IntroActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            LinkItTheme {
                IntroScreen(
                    onNavigateToHome = {
                        startActivity(
                            Intent().setClassName(
                                packageName,
                                "com.linkit.company.android.MainActivity",
                            )
                        )
                        finish()
                    },
                )
            }
        }
    }
}
