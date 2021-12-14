package com.upf.memorytrace_android.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.api.repository.UserRepository
import com.upf.memorytrace_android.ui.main.MainActivity
import com.upf.memorytrace_android.util.MemoryTraceConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        sendNotification(remoteMessage)
    }

    override fun onNewToken(token: String) {
        if (token.isBlank()) Log.e(TAG, "token is blank")
        else {
            //todo: globalScope 괜찮은가?
            GlobalScope.launch(Dispatchers.IO) {
                UserRepository.updateFcmToken(token)
            }
        }
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {

        //todo: navigation으로 어떻게 넘기는지 확인
        //bid, bgcolor, nickname, stickerImg, modifiedDate, title

        try {
            MemoryTraceConfig.bid = remoteMessage.data["bid"]?.toInt() ?: -1
            MemoryTraceConfig.did = remoteMessage.data["did"]?.toInt() ?: -1

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(
                this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT
            )

            val channelId = getString(R.string.default_notification_channel_id)
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_duckz)
                .setContentTitle(remoteMessage.notification?.title)
                .setContentText(remoteMessage.notification?.body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "덕지 알림",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            return
        }
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}