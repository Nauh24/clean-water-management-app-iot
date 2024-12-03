//
//
//class WebSocketClient {
//
//    private var socket: Socket? = null
//
//    // Kết nối WebSocket
//    fun connect() {
//        try {
//            Log.d("WebSocket", "Connecting to server...")
//            Log.d("WebSocket", "Socket: ${socket?.connected()}")
//            // Nếu socket đã kết nối rồi, không kết nối lại
//            if (socket?.connected() == true) {
//                return
//            }
//
////            socket = IO.socket("https://iot-backend-project.onrender.com") // URL của server WebSocket
//            socket = IO.socket("http://10.0.2.2:4000/") // URL của server WebSocket
//            socket?.connect()
//            Log.d("WebSocket", "Socket connected: ${socket?.connected()}")
//
//            // Lắng nghe sự kiện kết nối và ngắt kết nối
//            socket?.on(Socket.EVENT_CONNECT) {
//                Log.d("WebSocket", "Connected to server")
//            }
//
//            socket?.on(Socket.EVENT_DISCONNECT) {
//                Log.d("WebSocket", "Disconnected from server")
//            }
//
//            // Lắng nghe sự kiện "message" từ server
//            socket?.on("message") { args ->
//                val message = args[0] as String
//                Log.d("WebSocket", "Received message: $message")
//                // Gửi dữ liệu message tới ViewModel
//                WebSocketReceiver.onUpdateReceived(message)
//            }
//
//            // Lắng nghe sự kiện "update" từ server (thông báo mới)
//            socket?.on("update") { args ->
//                val updateMessage = args[0] as String
//                Log.d("WebSocket", "Received update: $updateMessage")
//                // Gửi dữ liệu update tới ViewModel
//                WebSocketReceiver.onUpdateReceived(updateMessage)
//            }
//
//        } catch (e: Exception) {
//            Log.e("WebSocket", "Error: ${e.message}")
//            e.printStackTrace()
//        }
//    }
//
//    // Ngắt kết nối WebSocket
//    fun disconnect() {
//        socket?.disconnect()
//        Log.d("WebSocket", "WebSocket disconnected")
//    }
//}




//class WebSocketClient {
//
//    private var webSocketClient: WebSocketClient? = null
//
//    // Kết nối WebSocket
//    fun connect() {
//        try {
//            Log.d("WebSocket", "Connecting to server...")
//
//            // Tạo URI cho WebSocket server
//            val uri = URI("ws://10.0.2.2:4000") // Địa chỉ WebSocket server của bạn (localhost cho emulator Android)
//
//            // Khởi tạo WebSocket client
//            webSocketClient = object : WebSocketClient(uri) {
//                override fun onOpen(handshakedata: ServerHandshake?) {
//                    Log.d("WebSocket", "Connected to WebSocket server")
//                    // Gửi một thông điệp đến server khi kết nối thành công
//                    send("Hello from Android Client!")
//                }
//
//                override fun onMessage(message: String?) {
//                    Log.d("WebSocket", "Received message: $message")
//                    // Xử lý tin nhắn nhận từ server
//                    // Gửi dữ liệu message tới ViewModel hoặc UI
//                }
//
//                override fun onClose(code: Int, reason: String?, remote: Boolean) {
//                    Log.d("WebSocket", "Disconnected from WebSocket server")
//                }
//
//                override fun onError(ex: Exception?) {
//                    Log.e("WebSocket", "Error: ${ex?.message}")
//                }
//            }
//
//            // Kết nối tới server
//            webSocketClient?.connect()
//
//        } catch (e: Exception) {
//            Log.e("WebSocket", "Error: ${e.message}")
//            e.printStackTrace()
//        }
//    }
//
//    // Ngắt kết nối WebSocket
//    fun disconnect() {
//        webSocketClient?.close()
//        Log.d("WebSocket", "WebSocket disconnected")
//    }
//}