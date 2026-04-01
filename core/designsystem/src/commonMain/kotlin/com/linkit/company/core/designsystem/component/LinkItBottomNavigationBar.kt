package com.linkit.company.core.designsystem.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.linkit.company.core.designsystem.theme.G8
import com.linkit.company.core.designsystem.theme.G9
import com.linkit.company.core.designsystem.theme.LinkItTextStyle
import com.linkit.company.core.designsystem.theme.White

enum class BottomNavTab(
    val label: String,
    val icon: ImageVector,
) {
    Map("지도", Icons.Filled.Home),
    Storage("보관함", Icons.Filled.Home),
    Explore("탐색", Icons.Filled.Search),
}

@Composable
fun LinkItBottomNavigationBar(
    selectedTab: BottomNavTab,
    onTabSelected: (BottomNavTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier.height(60.dp),
        containerColor = White,
        tonalElevation = 0.dp,
    ) {
        BottomNavTab.entries.forEach { tab ->
            val isSelected = tab == selectedTab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.label,
                        modifier = Modifier.size(20.dp),
                    )
                },
                label = {
                    Text(
                        text = tab.label,
                        style = LinkItTextStyle.xs,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = G9,
                    selectedTextColor = G9,
                    unselectedIconColor = G8,
                    unselectedTextColor = G8,
                    indicatorColor = White,
                ),
            )
        }
    }
}