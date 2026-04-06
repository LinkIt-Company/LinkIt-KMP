package com.linkit.company.feature.schedule.navigator

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import com.linkit.company.core.navigation.navigator.feature.ScheduleNavigator
import com.linkit.company.core.navigation.navigator.startActivity
import com.linkit.company.feature.schedule.ScheduleActivity
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@ContributesBinding(AppScope::class)
@Inject
class ScheduleNavigatorImpl : ScheduleNavigator {

    override fun navigateWithLauncher(
        activity: ComponentActivity,
        intentBuilder: (Intent.() -> Intent)?,
        launcher: ActivityResultLauncher<Intent>?,
    ) = startActivity<ScheduleActivity>(activity, intentBuilder, launcher)
}
