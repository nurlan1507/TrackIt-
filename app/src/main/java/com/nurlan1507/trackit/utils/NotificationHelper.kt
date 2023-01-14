package com.nurlan1507.trackit.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.nurlan1507.trackit.MainActivity
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.data.notifications.Notification


class NotificationHelper(val context: Context) {
    private val channelId = "TrackIt notification channel id"
    private val notificationId = 1

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId,channelId, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Trackit notification id"
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
//    @RequiresApi(Build.VERSION_CODES.S)
//    fun createNotification(title:String, message:String){
//        createNotificationChannel()
//        val intent = Intent(context, MainActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_MUTABLE)
//        val notification = NotificationCompat.Builder(context,channelId)
//            .setContentText(message)
//            .setContentTitle(title)
//            .setContentIntent(pendingIntent)
//            .build()
//        NotificationManagerCompat.from(context).notify(notificationId,notification)
//    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun createNotification(title:String, message:String){
        createNotificationChannel()
        val intent = Intent(context,MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0,intent,PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(context,channelId)
            .setSmallIcon(R.drawable.avatar_shape)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        NotificationManagerCompat.from(context).notify(notificationId,notification)
    }

}