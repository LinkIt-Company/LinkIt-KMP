package com.linkit.company.feature.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onOpenSchedule: () -> Unit,
    navigateToScheduleEdit: () -> Unit,
) {
    var showScheduleSheet by rememberSaveable { mutableStateOf(true) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Map Screen",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onOpenSchedule,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Open Schedule")
        }
    }

    if (showScheduleSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { showScheduleSheet = false },
        ) {
            ScheduleScreen(
                onNavigateToScheduleEdit = {
                    coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                        showScheduleSheet = false
                    }
                    navigateToScheduleEdit()
                },
                onBack = {
                    coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                        showScheduleSheet = false
                    }
                },
            )
        }
    }
}
