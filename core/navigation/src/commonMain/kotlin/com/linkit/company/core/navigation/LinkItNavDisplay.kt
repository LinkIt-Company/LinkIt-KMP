package com.linkit.company.core.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.defaultPopTransitionSpec
import androidx.navigation3.ui.defaultPredictivePopTransitionSpec
import androidx.navigation3.ui.defaultTransitionSpec
import androidx.navigationevent.NavigationEvent

val LocalLinkItNavigator = compositionLocalOf<LinkItNavigator> {
    error("LocalLinkItNavigator not provided")
}

/**
 * 백스택과 entryProvider를 받아 NavDisplay를 구성합니다.
 * 단일 백스택 기반의 네비게이션에 사용합니다. (e.g. ScheduleNavDisplay)
 *
 * @param backStack 현재 네비게이션 백스택
 * @param onBack 뒤로가기 시 호출되는 콜백
 * @param modifier NavDisplay에 적용할 Modifier
 * @param entryDecorators NavEntry에 적용할 데코레이터 목록 (e.g. SaveableStateHolder, ViewModelStore)
 * @param contentAlignment NavDisplay 내 콘텐츠 정렬 방식
 * @param sceneStrategy Scene 전환 전략
 * @param sizeTransform Scene 전환 시 크기 변환 애니메이션
 * @param transitionSpec 화면 전환 애니메이션 스펙
 * @param popTransitionSpec 뒤로가기 전환 애니메이션 스펙
 * @param predictivePopTransitionSpec 예측형 뒤로가기 전환 애니메이션 스펙
 * @param entryProvider NavKey를 받아 NavEntry를 생성하는 팩토리 함수
 */
@Composable
fun LinkItNavDisplay(
    backStack: NavBackStack<NavKey>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    entryDecorators: List<NavEntryDecorator<NavKey>> = emptyList(),
    contentAlignment: Alignment = Alignment.TopCenter,
    sceneStrategy: SceneStrategy<NavKey> = SinglePaneSceneStrategy(),
    sizeTransform: SizeTransform? = null,
    transitionSpec: (AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform)? = null,
    popTransitionSpec: (AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform)? = null,
    predictivePopTransitionSpec: (AnimatedContentTransitionScope<Scene<NavKey>>.(@NavigationEvent.SwipeEdge Int) -> ContentTransform)? = null,
    entryProvider: (key: NavKey) -> NavEntry<NavKey>,
) {
    val transitionSpec = transitionSpec ?: defaultTransitionSpec()
    val popTransitionSpec = popTransitionSpec ?: defaultPopTransitionSpec()
    val predictivePopTransitionSpec =
        predictivePopTransitionSpec ?: defaultPredictivePopTransitionSpec()

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = onBack,
        entryDecorators = entryDecorators,
        contentAlignment = contentAlignment,
        sceneStrategy = sceneStrategy,
        sizeTransform = sizeTransform,
        transitionSpec = transitionSpec,
        popTransitionSpec = popTransitionSpec,
        predictivePopTransitionSpec = predictivePopTransitionSpec,
        entryProvider = entryProvider,
    )
}

/**
 * 이미 decorated된 NavEntry 목록을 받아 NavDisplay를 구성합니다.
 * [NavigationState.toDecoratedEntries]와 함께 멀티 백스택 네비게이션에 사용합니다. (e.g. HomeNavDisplay)
 *
 * @param entries decorated된 NavEntry 목록 ([NavigationState.toDecoratedEntries]로 생성)
 * @param modifier NavDisplay에 적용할 Modifier
 * @param contentAlignment NavDisplay 내 콘텐츠 정렬 방식
 * @param sceneStrategy Scene 전환 전략
 * @param sizeTransform Scene 전환 시 크기 변환 애니메이션
 * @param transitionSpec 화면 전환 애니메이션 스펙
 * @param popTransitionSpec 뒤로가기 전환 애니메이션 스펙
 * @param predictivePopTransitionSpec 예측형 뒤로가기 전환 애니메이션 스펙
 * @param onBack 뒤로가기 시 호출되는 콜백
 */
@Composable
fun LinkItNavDisplay(
    entries: List<NavEntry<NavKey>>,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopCenter,
    sceneStrategy: SceneStrategy<NavKey> = SinglePaneSceneStrategy(),
    sizeTransform: SizeTransform? = null,
    transitionSpec: (AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform)? = null,
    popTransitionSpec: (AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform)? = null,
    predictivePopTransitionSpec: (AnimatedContentTransitionScope<Scene<NavKey>>.(@NavigationEvent.SwipeEdge Int) -> ContentTransform)? = null,
    onBack: () -> Unit,
) {
    val transitionSpec = transitionSpec ?: defaultTransitionSpec()
    val popTransitionSpec = popTransitionSpec ?: defaultPopTransitionSpec()
    val predictivePopTransitionSpec =
        predictivePopTransitionSpec ?: defaultPredictivePopTransitionSpec()

    NavDisplay(
        entries = entries,
        modifier = modifier,
        contentAlignment = contentAlignment,
        sceneStrategy = sceneStrategy,
        sizeTransform = sizeTransform,
        transitionSpec = transitionSpec,
        popTransitionSpec = popTransitionSpec,
        predictivePopTransitionSpec = predictivePopTransitionSpec,
        onBack = onBack,
    )
}