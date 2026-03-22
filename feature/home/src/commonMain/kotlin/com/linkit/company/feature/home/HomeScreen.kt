package com.linkit.company.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import kotlinx.coroutines.launch

enum class HomeTab(val label: String) {
    Map("Map"),
    Storage("Storage"),
    Explore("Explore"),
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
