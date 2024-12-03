package com.nauh.waterqualitymonitor.data.network

import android.util.Log
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.android.service.MqttAndroidClient
import java.net.URI

class MQTTClient(private val serverUri: String, private val clientId: String) {

    private var mqttClient: MqttAndroidClient? = null

    // Kết nối tới MQTT server
    fun connect(onConnected: () -> Unit = {}, onError: (String) -> Unit = {}) {
        try {
            Log.d("MQTT", "Connecting to MQTT server...")

            // Tạo client MQTT
            mqttClient = MqttAndroidClient(
                null, // Android Context không cần thiết trong trường hợp này
                serverUri,
                clientId
            )

            // Tùy chọn kết nối MQTT
            val options = MqttConnectOptions().apply {
                isCleanSession = true
                keepAliveInterval = 20
                connectionTimeout = 10
            }

            // Kết nối tới MQTT server
            mqttClient?.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d("MQTT", "Connected to MQTT server")
                    onConnected()
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    val error = exception?.message ?: "Unknown error"
                    Log.e("MQTT", "Failed to connect: $error")
                    onError(error)
                }
            })

            // Đặt callback cho MQTT Client
            mqttClient?.setCallback(object : MqttCallback {
                override fun messageArrived(topic: String, message: MqttMessage?) {
                    val payload = message?.toString() ?: ""
                    Log.d("MQTT", "Message received on topic \"$topic\": $payload")
                    // Có thể bổ sung xử lý message ở đây
                }

                override fun connectionLost(cause: Throwable?) {
                    Log.e("MQTT", "Connection lost: ${cause?.message}")
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    Log.d("MQTT", "Message delivered successfully")
                }
            })
        } catch (e: Exception) {
            val error = e.message ?: "Unknown error"
            Log.e("MQTT", "Error while connecting: $error")
            onError(error)
        }
    }

    // Subscribe vào một topic
    fun subscribe(
        topic: String,
        qos: Int = 1,
        onMessageReceived: (String, String) -> Unit = { _, _ -> }
    ) {
        mqttClient?.subscribe(topic, qos, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d("MQTT", "Subscribed to topic \"$topic\"")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.e("MQTT", "Failed to subscribe: ${exception?.message}")
            }
        })

        mqttClient?.setCallback(object : MqttCallback {
            override fun messageArrived(topic: String, message: MqttMessage?) {
                val payload = message?.toString() ?: ""
                Log.d("MQTT", "Message received on topic \"$topic\": $payload")
                onMessageReceived(topic, payload)
            }

            override fun connectionLost(cause: Throwable?) {
                Log.e("MQTT", "Connection lost: ${cause?.message}")
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Log.d("MQTT", "Message delivered successfully")
            }
        })
    }

    // Ngắt kết nối khỏi MQTT server
    fun disconnect(onDisconnected: () -> Unit = {}, onError: (String) -> Unit = {}) {
        mqttClient?.disconnect(null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d("MQTT", "Disconnected from MQTT server")
                onDisconnected()
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                val error = exception?.message ?: "Unknown error"
                Log.e("MQTT", "Failed to disconnect: $error")
                onError(error)
            }
        })
    }
}
