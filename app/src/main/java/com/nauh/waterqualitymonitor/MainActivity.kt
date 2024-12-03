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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.nauh.waterqualitymonitor.data.model.StatsResponse
import com.nauh.waterqualitymonitor.data.network.MQTTClient
import com.nauh.waterqualitymonitor.data.network.RetrofitClient
import com.nauh.waterqualitymonitor.data.network.WebSocketReceiver
import com.nauh.waterqualitymonitor.data.network.WebSocketReceiver.mapToAlert
import com.nauh.waterqualitymonitor.data.network.WebSocketReceiver.mapToStatData
import com.nauh.waterqualitymonitor.ui.screen.Dashboard
import com.nauh.waterqualitymonitor.ui.screen.DashboardDetail
import com.nauh.waterqualitymonitor.ui.screen.Invoice
import com.nauh.waterqualitymonitor.ui.screen.LoginScreen
import com.nauh.waterqualitymonitor.ui.screen.Notification
//import com.nauh.waterqualitymonitor.ui.screen.NotificationDetail
import com.nauh.waterqualitymonitor.ui.screen.Statistics
import com.nauh.waterqualitymonitor.ui.theme.TopAppBarBackground
import com.nauh.waterqualitymonitor.ui.theme.WaterQualityMonitorTheme
import com.nauh.waterqualitymonitor.utils.AlertSaver
import com.nauh.waterqualitymonitor.utils.DataSaver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.java_websocket.client.WebSocketClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log
import kotlin.properties.Delegates

class MainActivity : ComponentActivity() {
    private lateinit var webSocketClient: WebSocketClient
    private lateinit var dataSaver: DataSaver
    private lateinit var alertSaver: AlertSaver
    private var delayTime by Delegates.notNull<Long>()
    private lateinit var fileName: String
    private lateinit var mqttClient: MQTTClient

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WaterQualityMonitorTheme {
                var showBottomBar by rememberSaveable { mutableStateOf(true) }
                val navController = rememberNavController()
                var username by remember { mutableStateOf("") }
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
                                    Dashboard(navController, username = username)
                                }
                            }
                            composable("login") {
                                LoginScreen(navController = navController, onLoginClick = { loggedInUser ->
                                    username = loggedInUser
                                    navController.navigate("dashboard")
                                })
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
//                            composable("notification_detail/{notificationId}") { backStackEntry ->
//                                val notificationId =
//                                    backStackEntry.arguments?.getString("notificationId")
//                                        ?.toIntOrNull()
//                                if (notificationId != null) {
//                                    NotificationDetail(
//                                        navController = navController,
//                                        notificationId = notificationId
//                                    )
//                                }
//                            }
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
//        try {
//            // Kết nối WebSocket
//            webSocketClient = WebSocketClient("ws://)
//            webSocketClient.connect()
//        } catch (e: Exception) {
//            Log.e("WebSocket", "Connection Error: ${e.message}", e)
//        }

//        val serverUri = "tcp://192.168.1.9:2403"
//        val clientId = "AndroidClient_${System.currentTimeMillis()}"

//        mqttClient = MQTTClient(serverUri, clientId)

        // Kết nối tới MQTT server
//        mqttClient.connect(
//            onConnected = {
//                Log.d("MQTT", "Connected to MQTT server")
//
//                // Subscribe vào topic
//                mqttClient.subscribe("/sensor/data") { topic, message ->
//                    Log.d("MQTT", "Received message: $message from topic: $topic")
//                }
//            },
//            onError = { error ->
//                Log.e("MQTT", "Error connecting to MQTT server: $error")
//            }
//        )

        // Khởi tạo DataSaver
        dataSaver = DataSaver(this)
        alertSaver = AlertSaver(this)

        // Khởi động WebSocket và lưu dữ liệu
        lifecycleScope.launch {
            try {
                WebSocketReceiver.connect(
                    context = this@MainActivity,
                    serverUrl = "ws://192.168.1.13:4000",
                    onMessageReceived = { message ->
                        try {
                            // Parse JSON từ thông điệp
                            val jsonObject = JSONObject(message)
                            val topic = jsonObject.optString("topic", "")
                            if (topic.isEmpty()) {
                                Log.e("WebSocketReceiver", "Topic not found in message")
                                return@connect
                            }

                            when (topic) {
                                "/sensor/data" -> {
                                    // Xử lý cho topic "/sensor/data"
                                    val dataString = jsonObject.optString("data", null)
                                    if (dataString != null) {
                                        try {
                                            // Parse chuỗi `data` thành JSON object
                                            val dataObject = JSONObject(dataString)
                                            // Ánh xạ thành `StatData`
                                            val statData = mapToStatData(dataObject)
                                            // Lưu dữ liệu
                                            dataSaver.saveNewData(Gson().toJson(statData), "data_tmp.json")
                                            Log.d("WebSocketReceiver", "Mapped StatData: $statData")
                                        } catch (e: Exception) {
                                            Log.e("WebSocketReceiver", "Failed to process sensor data: ${e.message}", e)
                                        }
                                    } else {
                                        Log.e("WebSocketReceiver", "Data not found in /sensor/data message")
                                    }
                                }
//                                "switch system" -> {
                                "notification" -> {
                                    // Xử lý cho topic "switch system"
//                                    val payload = jsonObject.optJSONObject("payload")
                                    val payload = jsonObject.optJSONObject("message")
                                    if (payload != null) {
                                        try {
                                            // Ánh xạ `payload` thành `Alert`
                                            val alert = mapToAlert(payload)
                                            // Lưu thông báo
                                            alertSaver.saveNewAlert(Gson().toJson(alert), "alerts_data.json")
                                            Log.d("WebSocketReceiver", "New alert saved: $alert")
                                        } catch (e: Exception) {
                                            Log.e("WebSocketReceiver", "Failed to process alert: ${e.message}", e)
                                        }
                                    } else {
                                        Log.e("WebSocketReceiver", "Payload not found for topic 'message'")
                                    }
                                }
                                else -> {
                                    Log.w("WebSocketReceiver", "Unknown topic received: $topic")
                                }
                            }

                        } catch (e: Exception) {
                            Log.e("WebSocketReceiver", "Error processing message: ${e.message}", e)
                        }
                    }
                )
                Toast.makeText(this@MainActivity, "WebSocket connected!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("MainActivity", "Error: ${e.message}")
                Toast.makeText(this@MainActivity, "WebSocket connection failed", Toast.LENGTH_SHORT).show()
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        // Ngắt kết nối WebSocket
        WebSocketReceiver.disconnect()
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WaterQualityMonitorTheme {

    }
}