package com.nauh.waterqualitymonitor


import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import com.nauh.waterqualitymonitor.data.model.StatsResponse
import com.nauh.waterqualitymonitor.data.network.RetrofitClient
import com.nauh.waterqualitymonitor.data.network.WebSocketClient
import com.nauh.waterqualitymonitor.ui.screen.Dashboard
import com.nauh.waterqualitymonitor.ui.screen.DashboardDetail
import com.nauh.waterqualitymonitor.ui.screen.Invoice
import com.nauh.waterqualitymonitor.ui.screen.Notification
import com.nauh.waterqualitymonitor.ui.screen.NotificationDetail
import com.nauh.waterqualitymonitor.ui.screen.Statistics
import com.nauh.waterqualitymonitor.ui.theme.TopAppBarBackground
import com.nauh.waterqualitymonitor.ui.theme.WaterQualityMonitorTheme

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    private lateinit var webSocketClient: WebSocketClient

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WaterQualityMonitorTheme {
                var showBottomBar by rememberSaveable { mutableStateOf(true) }
                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()

                showBottomBar = when (backStackEntry?.destination?.route) {
                    "dashboard_detail/{chartType}" -> false
                    else -> true
                }
                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
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
                                    Invoice(navController)
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
                                val notificationId =
                                    backStackEntry.arguments?.getString("notificationId")
                                        ?.toIntOrNull()
                                if (notificationId != null) {
                                    NotificationDetail(
                                        navController = navController,
                                        notificationId = notificationId
                                    )
                                }
                            }
                            composable("dashboard_detail/{chartType}") { backStackEntry ->
                                val chartType =
                                    backStackEntry.arguments?.getString("chartType") ?: "turbidity"
                                DashboardDetail(navController, chartType)
                            }
                        }
                    }
                )
            }
        }

        // Kết nối WebSocket
        try {
            // Kết nối WebSocket
            webSocketClient = WebSocketClient()
            webSocketClient.connect()
            Log.d("WebSocket", "WebSocket connected")
        } catch (e: Exception) {
            Log.e("WebSocket", "Connection Error: ${e.message}", e)
        }
//
        // Gọi API
//        RetrofitClient.apiService.getDatas().enqueue(object : Callback<StatsResponse> {
//            override fun onResponse(call: Call<StatsResponse>, response: Response<StatsResponse>) {
//                if (response.isSuccessful) {
//                    val stats = response.body()?.metadata?.data ?: emptyList() // Xử lý null safety
//                    Log.d("API_SUCCESS", "Stats: $stats")
//
//                } else {
//                    Log.e("API_ERROR", "Error: ${response.errorBody()?.string()}")
//                }
//            }
//
//            override fun onFailure(call: Call<StatsResponse>, t: Throwable) {
//                Log.e("API_FAILURE", "Failed to connect to API: ${t.message}")
//                Toast.makeText(
//                    this@MainActivity,
//                    "API connection failed: ${t.message}",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        })

    }
    override fun onDestroy() {
        super.onDestroy()
        webSocketClient.disconnect()  // Ngắt kết nối WebSocket khi Activity bị hủy
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WaterQualityMonitorTheme {

    }
}