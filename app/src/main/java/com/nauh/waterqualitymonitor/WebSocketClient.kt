//package com.nauh.waterqualitymonitor
//
//import android.util.Log
//import io.socket.client.IO
//import io.socket.client.Socket
//
//class WebSocketClient {
//
//    private var socket: Socket? = null
//
//    // Kết nối WebSocket
//    fun connect() {
//        try {
//            socket = IO.socket("https://iot-backend-project.onrender.com") // Thay bằng URL của server WebSocket
//            socket?.connect()
//
//            // Lắng nghe sự kiện "message" từ server
//            socket?.on("message") { args ->
//                val message = args[0] as String
//                Log.d("WebSocket", "Received message: $message")
//            }
//
//            // Gửi thông điệp đến server
//            socket?.emit("message", "Hello from Android")
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    // Ngắt kết nối WebSocket
//    fun disconnect() {
//        socket?.disconnect()
//    }
//}
