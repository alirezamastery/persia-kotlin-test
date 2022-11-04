package com.persia.test.data.network.services.google

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.persia.test.R
import com.persia.test.global.AppPreferences
import com.persia.test.ui.panel.PanelActivity
import timber.log.Timber
import kotlin.random.Random


private const val CHANNEL_ID = "my_channel"

class FirebaseService : FirebaseMessagingService() {
    companion object {

        var token: String?
            get() {
                return AppPreferences.firebaseToken
            }
            set(value) {
                AppPreferences.firebaseToken = value
            }
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        Timber.i("/////////// new fcm token: $token")
        token = newToken
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val intent = Intent(this, PanelActivity::class.java)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt() // make random so the old notifs won't be overridden

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.data["data"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.ic_baseline_category_24)
            .setAutoCancel(true) // notification is deleted when clicked on
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "channelName"
        val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH).apply {
            description = " test desc"
            enableLights(true)
            lightColor = Color.BLUE
        }
        notificationManager.createNotificationChannel(channel)
    }
}