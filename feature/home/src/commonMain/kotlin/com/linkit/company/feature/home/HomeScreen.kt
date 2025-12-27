package com.linkit.company.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onNavigateToOnboarding: () -> Unit = {},
    onNavigateToSave: () -> Unit = {},
    onNavigateToShare: () -> Unit = {},
    onNavigateToStorage: () -> Unit = {},
    onNavigateToClassification: () -> Unit = {},
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Home Screen",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = onNavigateToOnboarding,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go to Onboarding")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = onNavigateToSave,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go to Save")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = onNavigateToShare,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go to Share")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = onNavigateToStorage,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go to Storage")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = onNavigateToClassification,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go to Classification")
            }
        }
    }
}
