package com.nauh.waterqualitymonitor

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Lấy thông tin từ RemoteMessage
        val title = remoteMessage.notification?.title ?: "Thông báo"
        val message = remoteMessage.notification?.body ?: "Bạn có thông báo mới."

        // Hiển thị thông báo
        sendNotification(title, message)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(title: String, message: String) {
        val channelId = "water_quality_channel"
        val notificationId = System.currentTimeMillis().toInt()

        // Tạo Intent mở ứng dụng
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Tạo thông báo
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notification_id)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Tạo Notification Channel (chỉ cần một lần)
        val channel = NotificationChannel(
            channelId,
            "Thông báo chất lượng nước",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Kênh thông báo về cảnh báo chất lượng nước"
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)


        // Kiểm tra quyền trước khi hiển thị thông báo
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            with(NotificationManagerCompat.from(this)) {
                notify(notificationId, builder.build())
            }
        } else {
            // Xử lý khi không có quyền
            Log.e("MyFirebaseMessagingService", "Permission to post notifications is not granted")
        }
    }
}
