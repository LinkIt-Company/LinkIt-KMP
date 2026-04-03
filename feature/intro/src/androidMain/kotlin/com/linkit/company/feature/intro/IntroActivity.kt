package com.linkit.company.feature.intro

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.linkit.company.core.common.extension.enableEdgeToEdgeConfig
import com.linkit.company.core.designsystem.theme.LinkItTheme
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.android.ActivityKey

@ContributesIntoMap(AppScope::class)
@ActivityKey(IntroActivity::class)
@Inject
class IntroActivity(
    private val viewModelFactory: ViewModelProvider.Factory,
) : ComponentActivity() {

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        get() = viewModelFactory

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
