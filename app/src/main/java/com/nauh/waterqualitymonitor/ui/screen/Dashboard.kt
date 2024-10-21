package com.nauh.waterqualitymonitor.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.vectorResource
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import com.nauh.waterqualitymonitor.R
import com.nauh.waterqualitymonitor.ui.components.TopBar
import com.nauh.waterqualitymonitor.ui.theme.Shapes
import com.nauh.waterqualitymonitor.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(pageTitle = "Tổng quan", onAccountClick = {}) // Thêm tên người dùng ở đây
        },
        content = { paddingValues ->
            // Nội dung của trang
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Card cho Độ Đục
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("dashboard_detail/turbidity")
                        },
                    shape = Shapes.medium
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Độ Đục",
                            style = Typography.titleMedium
                        )
                        Text(
                            text = "5 NTU",
                            style = Typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
                        )
                    }
                }

                // Card cho Độ Dẫn Điện (EC)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("dashboard_detail/ec")
                        },
                    shape = Shapes.medium
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Độ Dẫn Điện (EC)",
                            style = Typography.titleMedium
                        )
                        Text(
                            text = "200 µS/cm",
                            style = Typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
                        )
                    }
                }

                // Card cho Nhiệt Độ
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("dashboard_detail/temperature")
                        },
                    shape = Shapes.medium
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Nhiệt Độ",
                            style = Typography.titleMedium
                        )
                        Text(
                            text = "25°C",
                            style = Typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
                        )
                    }
                }

                // Card cho Trạng Thái Relay
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("dashboard_detail/relay")
                        },
                    shape = Shapes.medium
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Trạng Thái Relay",
                            style = Typography.titleMedium
                        )
                        Text(
                            text = "Bật",
                            style = Typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            }
        }
    )
}


