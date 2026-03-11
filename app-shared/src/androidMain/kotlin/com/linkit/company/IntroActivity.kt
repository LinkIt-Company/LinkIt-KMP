package com.linkit.company

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.linkit.company.core.designsystem.theme.LinkItTheme
import com.linkit.company.feature.intro.IntroScreen

class IntroActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            LinkItTheme {
                IntroScreen(
                    onNavigateToHome = {
                        startActivity(Intent(this@IntroActivity, MainActivity::class.java))
                        finish()
                    },
                )
            }
        }
    }
}
