package com.linkit.company.feature.schedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.linkit.company.core.common.extension.enableEdgeToEdgeConfig
import com.linkit.company.core.designsystem.theme.LinkItTheme
import com.linkit.company.feature.schedule.navigation.ScheduleNavDisplay
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.android.ActivityKey

@ContributesIntoMap(AppScope::class)
@ActivityKey
@Inject
class ScheduleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdgeConfig()
        super.onCreate(savedInstanceState)

        setContent {
            LinkItTheme {
                ScheduleNavDisplay(
                    onFinishActivity = this::finish,
                )
            }
        }
    }
}
