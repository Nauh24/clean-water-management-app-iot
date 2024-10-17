package com.nauh.waterqualitymonitor.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Dashboard(navController: NavController) {
    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Tổng Quan Chất Lượng Nước",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Card for Turbidity
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable{
                        navController.navigate("dashboard_detail/turbidity")
                    },
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Độ Đục",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "5 NTU",
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
                    )
                }
            }

            // Card for Conductivity (EC)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable{
                        navController.navigate("dashboard_detail/ec")
                    },
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Độ Dẫn Điện (EC)",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "200 µS/cm",
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
                    )
                }
            }

            // Card for Temperature
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable{
                        navController.navigate("dashboard_detail/temperature")
                    },
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Nhiệt Độ",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "25°C",
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
                    )
                }
            }

            // Card for Relay Status
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable{
                        navController.navigate("dashboard_detail/relay")
                    },
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Trạng Thái Relay",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Bật",
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
                    )
                }
            }
        }
    }
}
