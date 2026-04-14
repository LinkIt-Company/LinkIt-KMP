package com.linkit.company.core.navigation.navigator

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher

/**
 * Feature 모듈 간 Activity 전환에 사용하는 base interface.
 *
 * 각 Feature 모듈은 이 인터페이스를 상속한 마커 인터페이스를 `core:navigation`에 정의하고,
 * 구현체를 자신의 `androidMain`에 배치하여 DI로 바인딩한다.
 */
interface Navigator {

    /**
     * Activity 전환에 사용되는 메서드.
     *
     * @param activity 현재 Activity
     * @param intentBuilder 타겟 Activity에 전달하는 Intent 빌더
     */
    fun navigate(
        activity: ComponentActivity,
        intentBuilder: (Intent.() -> Intent)? = null,
    ) = navigateWithLauncher(activity, intentBuilder, null)

    /**
     * Activity 전환 및 결과 전달에 사용되는 메서드.
     *
     * @param activity 현재 Activity
     * @param intentBuilder 타겟 Activity에 전달하는 Intent 빌더
     * @param launcher 타겟 Activity로 이동 후 결과를 전달 받을 [ActivityResultLauncher]
     */
    fun navigateWithLauncher(
        activity: ComponentActivity,
        intentBuilder: (Intent.() -> Intent)? = null,
        launcher: ActivityResultLauncher<Intent>?,
    )
}

/**
 * Navigator 구현체에서 공통으로 사용하는 Activity 전환 헬퍼.
 *
 * @param T 타겟 Activity 클래스
 * @param activity 현재 Activity
 * @param intentBuilder Intent에 extra를 추가하는 빌더
 * @param launcher 결과를 전달 받을 [ActivityResultLauncher], null이면 단순 전환
 */
inline fun <reified T : ComponentActivity> Navigator.startActivity(
    activity: ComponentActivity,
    noinline intentBuilder: (Intent.() -> Intent)? = null,
    launcher: ActivityResultLauncher<Intent>? = null,
) {
    val intent = Intent(activity, T::class.java).let {
        intentBuilder?.invoke(it) ?: it
    }
    if (launcher != null) {
        launcher.launch(intent)
    } else {
        activity.startActivity(intent)
    }
}
