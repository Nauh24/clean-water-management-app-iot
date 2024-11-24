package com.nauh.waterqualitymonitor.data.network

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket

// Singleton để nhận các cập nhật từ WebSocket
object WebSocketReceiver {
    var onUpdateReceived: (String) -> Unit = {}

    fun setReceiver(onUpdate: (String) -> Unit) {
        onUpdateReceived = onUpdate
    }

    // Mở kết nối WebSocket
    fun connect() {
        WebSocketClient().connect()
    }

    // Ngắt kết nối WebSocket
    fun disconnect() {
        WebSocketClient().disconnect()
    }
}

class WebSocketClient {

    private var socket: Socket? = null

    // Kết nối WebSocket
    fun connect() {
        try {
            // Nếu socket đã kết nối rồi, không kết nối lại
            if (socket?.connected() == true) {
                return
            }

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
                // Gửi dữ liệu message tới ViewModel
                WebSocketReceiver.onUpdateReceived(message)
            }

            // Lắng nghe sự kiện "update" từ server (thông báo mới)
            socket?.on("update") { args ->
                val updateMessage = args[0] as String
                Log.d("WebSocket", "Received update: $updateMessage")
                // Gửi dữ liệu update tới ViewModel
                WebSocketReceiver.onUpdateReceived(updateMessage)
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
