package com.nauh.waterqualitymonitor

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
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
import com.nauh.waterqualitymonitor.ui.screen.*
import com.nauh.waterqualitymonitor.ui.theme.TopAppBarBackground
import io.sentry.compose.withSentryObservableEffect
import com.nauh.waterqualitymonitor.ui.theme.WaterQualityMonitorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContent {
            WaterQualityMonitorTheme {
                var showBottomBar by rememberSaveable { mutableStateOf(true) }
                val navController = rememberNavController().withSentryObservableEffect()
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
                                    Invoice()
                                }
                            }
                            composable("statistics") {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding),
                                ) {
                                    Statistics()
                                }
                            }
                            composable("notifications") {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding),
                                ) {
                                    Notification()
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WaterQualityMonitorTheme {
    }
}