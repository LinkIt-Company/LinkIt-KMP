package com.linkit.company.feature.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.launch

enum class HomeTab(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    Map("지도", Icons.Filled.Map, Icons.Outlined.Map),
    Storage("보관함", Icons.Filled.Storage, Icons.Outlined.Storage),
    Explore("탐색", Icons.Filled.Explore, Icons.Outlined.Explore),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
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
                    val selected = selectedTab == tab
                    NavigationBarItem(
                        selected = selected,
                        onClick = { selectedTabIndex = index },
                        label = { Text(tab.label) },
                        icon = {
                            Icon(
                                imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.label,
                            )
                        },
                    )
                }
            }
        }
    ) { paddingValues ->
        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "HomeTabContent",
        ) { tab ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when (tab) {
                    HomeTab.Map -> mapContent { showScheduleSheet = true }
                    HomeTab.Storage -> storageContent()
                    HomeTab.Explore -> exploreContent()
                }
            }
        }
    }

    if (showScheduleSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { showScheduleSheet = false },
        ) {
            scheduleSheetContent {
                coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                    showScheduleSheet = false
                }
            }
        }
    }
}
