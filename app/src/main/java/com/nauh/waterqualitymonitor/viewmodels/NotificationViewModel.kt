
package com.nauh.waterqualitymonitor.viewmodels
import android.content.Context
import androidx.lifecycle.ViewModel
import com.nauh.waterqualitymonitor.data.model.Alert
import com.nauh.waterqualitymonitor.data.network.WebSocketReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject

class NotificationViewModel(context: Context) : ViewModel() {
    private val _sensorAlerts = MutableStateFlow<List<Alert>>(emptyList())
    val sensorAlerts: StateFlow<List<Alert>> = _sensorAlerts

    private val _alertNotifications = MutableStateFlow<List<Alert>>(emptyList())
    val alertNotifications: StateFlow<List<Alert>> = _alertNotifications

    val isWebSocketConnected = MutableStateFlow(false)

    init {
        WebSocketReceiver.connect(
            context,
            "ws://192.168.1.13:4000",
            onMessageReceived = { message ->
                // Parse dữ liệu JSON
                val jsonObject = JSONObject(message)
                val topic = jsonObject.getString("topic")
                val payload = jsonObject.getJSONObject("payload")

                val alert = Alert(
//                    id = payload.getString("_id"),
//                    alertType = payload.getString("alertType"),
                    message = payload.getString("message"),
                    createdAt = payload.getString("createdAt")
                )

                // Lọc dữ liệu theo topic
                when (topic) {
                    "sensorData" -> {
                        val currentAlerts = _sensorAlerts.value.toMutableList()
                        currentAlerts.add(alert)
                        _sensorAlerts.value = currentAlerts
                    }
                    "alert" -> {
                        val currentNotifications = _alertNotifications.value.toMutableList()
                        currentNotifications.add(alert)
                        _alertNotifications.value = currentNotifications
                    }
                }
            },
//            onConnectionChanged = { connected ->
//                isWebSocketConnected.value = connected
//            }
        )
    }
}
