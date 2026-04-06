package com.linkit.company.feature.home.navigator

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import com.linkit.company.core.navigation.navigator.feature.HomeNavigator
import com.linkit.company.core.navigation.navigator.startActivity
import com.linkit.company.feature.home.HomeActivity
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@ContributesBinding(AppScope::class)
@Inject
class HomeNavigatorImpl : HomeNavigator {

    override fun navigateWithLauncher(
        activity: ComponentActivity,
        intentBuilder: (Intent.() -> Intent)?,
        launcher: ActivityResultLauncher<Intent>?,
    ) = startActivity<HomeActivity>(activity, intentBuilder, launcher)
}
