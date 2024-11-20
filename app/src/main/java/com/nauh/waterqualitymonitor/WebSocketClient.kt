package com.nauh.waterqualitymonitor

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket

class WebSocketClient {

    private var socket: Socket? = null

    // Kết nối WebSocket
    fun connect() {
        try {
            socket = IO.socket("https://iot-backend-project.onrender.com") // URL của server WebSocket
            socket?.connect()

            // Lắng nghe sự kiện kết nối và ngắt kết nối
            socket?.on(Socket.EVENT_CONNECT) {
                Log.d("WebSocket", "Connected to server")
            }

            socket?.on(Socket.EVENT_DISCONNECT) {
                Log.d("WebSocket", "Disconnected from server")
            }

            // Lắng nghe sự kiện "message" từ server
            socket?.on("message") { args ->
                val message = args[0] as String
                Log.d("WebSocket", "Received message: $message")
            }

            // Lắng nghe thêm sự kiện khác, ví dụ "update"
            socket?.on("update") { args ->
                val updateMessage = args[0] as String
                Log.d("WebSocket", "Received update: $updateMessage")
            }

            // Gửi thông điệp đến server
            if (socket?.connected() == true) {
                socket?.emit("message", "Hello from Android")
            } else {
                Log.e("WebSocket", "Not connected")
            }
        } catch (e: Exception) {
            Log.e("WebSocket", "Error: ${e.message}")
            e.printStackTrace()
        }
    }

    // Ngắt kết nối WebSocket
    fun disconnect() {
        socket?.disconnect()
        Log.d("WebSocket", "WebSocket disconnected")
    }
}
