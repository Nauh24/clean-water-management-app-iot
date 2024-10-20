package com.nauh.waterqualitymonitor



import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nauh.waterqualitymonitor.ui.screen.Dashboard
import com.nauh.waterqualitymonitor.ui.screen.DashboardDetail
import com.nauh.waterqualitymonitor.ui.screen.Invoice
import com.nauh.waterqualitymonitor.ui.screen.Notification
import com.nauh.waterqualitymonitor.ui.screen.NotificationDetail
import com.nauh.waterqualitymonitor.ui.screen.Statistics
import com.nauh.waterqualitymonitor.ui.theme.TopAppBarBackground
import com.nauh.waterqualitymonitor.ui.theme.WaterQualityMonitorTheme


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WaterQualityMonitorTheme {
                var showBottomBar by rememberSaveable { mutableStateOf(true) }
                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()
                Scaffold(
                    bottomBar = {
                        NavigationBar(containerColor = TopAppBarBackground) {
                            NavigationBarItem(
                                selected = backStackEntry?.destination?.route == "dashboard",
                                onClick = { navController.navigate("dashboard") },
                                label = {
                                    Text("Tổng quan")
                                },
                                icon = {
                                    Icon(
                                        painterResource(id = R.drawable.home),
                                        contentDescription = "Home"
                                    )
                                }
                            )
                            NavigationBarItem(
                                selected = backStackEntry?.destination?.route == "invoice",
                                onClick = { navController.navigate("invoice") },
                                label = {
                                    Text("Hóa đơn")
                                },
                                icon = {
                                    Icon(
                                        painterResource(id = R.drawable.invoice),
                                        contentDescription = "invoice"
                                    )
                                }
                            )
                            NavigationBarItem(
                                selected = backStackEntry?.destination?.route == "statistics",
                                onClick = { navController.navigate("statistics") },
                                label = {
                                    Text("Thống kê")
                                },
                                icon = {
                                    Icon(
                                        painterResource(id = R.drawable.bar_chart),
                                        contentDescription = "Statistics"
                                    )
                                }
                            )
                            NavigationBarItem(
                                selected = backStackEntry?.destination?.route == "notifications",
                                onClick = { navController.navigate("notifications") },
                                label = {
                                    Text("Thông báo")
                                },
                                icon = {
                                    Icon(
                                        painterResource(id = R.drawable.notifications),
                                        contentDescription = "Notifications"
                                    )
                                }
                            )
                        }
                    },
                    content = { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = "dashboard",
                        ) {
                            composable("dashboard") {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding),
                                ) {
                                    Dashboard(navController)
                                }
                            }
                            composable("invoice") {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding),
                                ) {
                                    Invoice(navController, username = "Nauh")
                                }
                            }
                            composable("statistics") {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding),
                                ) {
                                    Statistics(navController)
                                }
                            }
                            composable("notifications") {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding),
                                ) {
                                    Notification(navController)
                                }
                            }
                            composable("notification_detail/{notificationId}") { backStackEntry ->
                                val notificationId = backStackEntry.arguments?.getString("notificationId")?.toIntOrNull()
                                if (notificationId != null) {
                                    NotificationDetail(navController = navController, notificationId = notificationId)
                                }
                            }
                            composable("dashboard_detail/{chartType}") { backStackEntry ->
                                val chartType = backStackEntry.arguments?.getString("chartType") ?: "turbidity"
                                DashboardDetail(navController, chartType)
                            }
                        }
                    }
                )
            }
        }

    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WaterQualityMonitorTheme {

    }
}