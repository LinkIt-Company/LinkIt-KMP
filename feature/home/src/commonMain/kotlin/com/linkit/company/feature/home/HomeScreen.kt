package com.linkit.company.feature.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.savedstate.serialization.SavedStateConfiguration
import com.linkit.company.core.navigation.LinkItNavDisplay
import com.linkit.company.core.navigation.LinkItNavKey
import com.linkit.company.core.navigation.LinkItNavigator
import com.linkit.company.core.navigation.LocalLinkItNavigator
import com.linkit.company.core.navigation.rememberNavigationState
import com.linkit.company.core.navigation.toDecoratedEntries
import com.linkit.company.feature.explore.navigation.exploreEntry
import com.linkit.company.feature.home.navigation.TopLevelRoutes
import com.linkit.company.feature.map.navigation.mapEntry
import com.linkit.company.feature.storage.navigation.storageEntry

@Composable
fun LinkItNavigationBar(
    currentTab: NavKey,
    onTabSelected: (NavKey) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier.fillMaxWidth(),
    ) {
        for ((key, value) in TopLevelRoutes) {
            NavigationBarItem(
                label = { Text(value.label) },
                icon = {},
                selected = currentTab == key,
                onClick = { onTabSelected(key) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    savedStateConfiguration: SavedStateConfiguration,
    modifier: Modifier = Modifier,
) {
    val navigationState =
        rememberNavigationState(
            savedStateConfiguration = savedStateConfiguration,
            startRoute = LinkItNavKey.Map,
            topLevelRoutes = TopLevelRoutes.keys
        )

    val navigator = remember(navigationState) { LinkItNavigator(navigationState) }

    val entryProvider = entryProvider {
        mapEntry(
            onOpenSchedule = {
                navigator.navigate(LinkItNavKey.ScheduleEdit)
            },
        )
        storageEntry()
        exploreEntry()
    }

//    var showScheduleSheet by rememberSaveable { mutableStateOf(false) }
//    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
//    val coroutineScope = rememberCoroutineScope()

    CompositionLocalProvider(LocalLinkItNavigator provides navigator) {
        Scaffold(
            modifier = modifier.fillMaxSize().systemBarsPadding(),
            bottomBar = {
                LinkItNavigationBar(
                    currentTab = navigationState.currentTopLevelRoute,
                    onTabSelected = { key -> navigator.navigate(key) },
                )
            }
        ) { paddingValues ->
            LinkItNavDisplay(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                onBack = navigator::navigateBack,
                entries = navigationState.toDecoratedEntries(entryProvider),
            )
        }

//        if (showScheduleSheet) {
//            ModalBottomSheet(
//                sheetState = sheetState,
//                onDismissRequest = { showScheduleSheet = false },
//            ) {
//                ScheduleScreen(
//                    onNavigateToScheduleEdit = {
//                        coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
//                            showScheduleSheet = false
//                        }
//                        navigator.navigate(ScheduleEdit)
//                    },
//                    onBack = {
//                        coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
//                            showScheduleSheet = false
//                        }
//                    },
//                )
//            }
//        }
    }
}
