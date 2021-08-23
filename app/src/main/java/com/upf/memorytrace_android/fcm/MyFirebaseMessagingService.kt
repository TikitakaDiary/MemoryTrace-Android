package com.upf.memorytrace_android.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.ArrayMap
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.upf.memorytrace_android.R
import com.upf.memorytrace_android.api.repository.UserRepository
import com.upf.memorytrace_android.ui.MainActivity
import com.upf.memorytrace_android.util.MemoryTraceConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        /*
        키는 bid, bgcolor, nickname, stickerImg, modifiedDate, title 이렇게 있습니다
         */
        if (remoteMessage.data.isNotEmpty()) {
            val message = remoteMessage.data["bid"]
            sendNotification(message!!)
        }

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

    private fun sendNotification(bid: String) {

        //todo: navigation으로 어떻게 넘기는지 확인
        try{
            MemoryTraceConfig.bid = bid.toInt()
        }catch (e:Exception){

        }
        
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_google)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.fcm_new_diary))
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}