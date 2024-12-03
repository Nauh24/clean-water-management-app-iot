package com.nauh.waterqualitymonitor.data.network

import android.content.Context
import android.util.Log
import com.nauh.waterqualitymonitor.data.model.Alert
import com.nauh.waterqualitymonitor.data.model.StatData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.json.JSONObject
import java.net.URI
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object WebSocketReceiver {

    private var webSocketClient: WebSocketClient? = null

    // Hàm ánh xạ JSON sang StatData
    fun mapToStatData(jsonObject: JSONObject): StatData {
        val createdAt =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(Date())
        return StatData(
//            _id = "",
            temperature = jsonObject.optDouble("temperature", 0.0),
            tds = jsonObject.optDouble("tds", 0.0),
            flowRate = jsonObject.optDouble("flowRate", 0.0),
            relay = jsonObject.optInt("pumpState", 0),
            createdAt = createdAt,
//            __v = 0
        )
    }

    fun mapToAlert(payload: JSONObject): Alert {
//        val id = "" // Sinh ngẫu nhiên hoặc để trống
//        val alertType = payload.optString("alert_type", "")
        val message = payload.optString("message", "No message")
        val createdAt =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(Date())

        return Alert(
//            alertType = alertType,
            message = message,
            createdAt = createdAt
        )
    }

    /**
     * Kết nối tới WebSocket server.
     * @param context Context từ Activity.
     * @param serverUrl Địa chỉ WebSocket server.
     * @param onMessageReceived Callback xử lý khi nhận được dữ liệu.
     */
    fun connect(context: Context, serverUrl: String, onMessageReceived: (String) -> Unit) {
        Log.d("WebSocketReceiver", "Connecting to WebSocket server: $serverUrl")
        if (webSocketClient == null) {
            Log.d("WebSocketReceiver", "Creating new WebSocket client")
            webSocketClient = object : WebSocketClient(URI(serverUrl)) {
                override fun onOpen(handshakedata: ServerHandshake?) {
                    Log.d("WebSocketReceiver", "Connected to WebSocket server")
                    send("Hello from Android Client!")
                }

                override fun onMessage(message: String?) {
                    if (message != null) {
                        Log.d("WebSocketReceiver", "Message received: $message")
                        onMessageReceived(message) // Gọi callback khi nhận dữ liệu
                    }
                }

                //                override fun onClose(code: Int, reason: String?, remote: Boolean) {
//                    Log.d("WebSocketReceiver", "WebSocket disconnected: $reason")
//                    // Thử kết nối lại nếu cần thiết
//                    if (!remote) {
//                        webSocketClient?.reconnect()
//                    }
//                }
                override fun onClose(code: Int, reason: String?, remote: Boolean) {
                    Log.d("WebSocketReceiver", "WebSocket disconnected: $reason")
                    if (!remote) {
                        Log.d("WebSocketReceiver", "Attempting to reconnect after delay...")
                        GlobalScope.launch {
                            kotlinx.coroutines.delay(5000) // Chờ 5 giây trước khi kết nối lại
                            connect(context, serverUrl, onMessageReceived)
                        }
                    }
                }


                override fun onError(ex: Exception?) {
                    Log.e("WebSocketReceiver", "WebSocket error: ${ex?.message}")
                }
            }
        }
        webSocketClient?.connect()
    }

    /**
     * Ngắt kết nối WebSocket.
     */
    fun disconnect() {
        webSocketClient?.close()
        webSocketClient = null
    }
}
