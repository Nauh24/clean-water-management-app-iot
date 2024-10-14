package com.nauh.waterqualitymonitor.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun Dashboard(navController: NavController) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column {
            Text(text = "Water Usage Overview")
            // Mock data for display
            Text(text = "Total Water Used: 1500L")
            Text(text = "Warning: Contaminated Water!")
            Button(onClick = { navController.navigate("invoice") }) {
                Text("View Invoices")
            }
            Button(onClick = { navController.navigate("statistics") }) {
                Text("View Statistics")
            }
            Button(onClick = { navController.navigate("warnings") }) {
                Text("View Warnings")
            }
        }
    }
}
