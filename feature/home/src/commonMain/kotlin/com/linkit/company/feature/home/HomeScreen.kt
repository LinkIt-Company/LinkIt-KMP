package com.linkit.company.feature.home

<<<<<<< HEAD
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
=======
import androidx.compose.foundation.layout.Box
>>>>>>> origin/feature/#4-navigation
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
<<<<<<< HEAD
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.linkit.company.core.common.architecture.popup.PopupExposureType
import com.linkit.company.feature.home.sample.HomeViewModel
=======
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
>>>>>>> origin/feature/#4-navigation

enum class HomeTab(val label: String) {
    Map("Map"),
    Storage("Storage"),
    Explore("Explore"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
<<<<<<< HEAD
fun HomeScreen() {
    val homeViewModel by lazy { HomeViewModel(SavedStateHandle()) }
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = Unit) {
        homeViewModel.sideEffect.collect {
            when (it) {
                else -> {}
=======
fun HomeScreen(
    mapContent: @Composable (onOpenSchedule: () -> Unit) -> Unit,
    storageContent: @Composable () -> Unit,
    exploreContent: @Composable () -> Unit,
    scheduleSheetContent: @Composable (onDismissSheet: () -> Unit) -> Unit,
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val selectedTab = HomeTab.entries[selectedTabIndex]
    var showScheduleSheet by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            NavigationBar {
                HomeTab.entries.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTabIndex = index },
                        label = { Text(tab.label) },
                        icon = {},
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                HomeTab.Map -> mapContent { showScheduleSheet = true }
                HomeTab.Storage -> storageContent()
                HomeTab.Explore -> exploreContent()
>>>>>>> origin/feature/#4-navigation
            }
        }
    }

<<<<<<< HEAD
    LaunchedEffect(key1 = Unit) {
        homeViewModel.popupEffect.collect { exposureType ->
            when (exposureType) {
                is PopupExposureType.Toast -> {

                }

                is PopupExposureType.Dialog -> {
                }
            }
        }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Home Screen",
                style = MaterialTheme.typography.headlineMedium
            )

            if (uiState.isLoading) {
                val infiniteTransition = rememberInfiniteTransition()
                val rotation by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = 1000,
                            easing = LinearEasing
                        ),
                        repeatMode = RepeatMode.Restart
                    )
                )
                Layout(
                    modifier = Modifier.drawBehind {
                        drawRect(
                            color = Color.Black.copy(alpha = 0.5f),
                            size = size
                        )

                        val strokeWidth = 8.dp.toPx()
                        val diameter = 48.dp.toPx()
                        val topLeft = Offset(
                            x = (size.width - diameter) / 2,
                            y = (size.height - diameter) / 2
                        )

                        drawArc(
                            color = Color.White,
                            startAngle = rotation,
                            sweepAngle = 270f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = Size(diameter, diameter),
                            style = Stroke(
                                width = strokeWidth,
                                cap = StrokeCap.Round
                            )
                        )
                    },
                ) { measureables, constraints ->
                    layout(constraints.maxWidth, constraints.maxHeight) {

                    }
=======
    if (showScheduleSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { showScheduleSheet = false },
        ) {
            scheduleSheetContent {
                coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                    showScheduleSheet = false
>>>>>>> origin/feature/#4-navigation
                }
            }
        }
    }
}
