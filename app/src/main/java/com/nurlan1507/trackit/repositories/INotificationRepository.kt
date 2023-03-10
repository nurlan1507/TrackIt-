package com.nurlan1507.trackit.repositories

import com.nurlan1507.trackit.data.notifications.INotification
import com.nurlan1507.trackit.helpers.ApiResult


interface INotificationRepository {
    suspend fun getNotifications():ApiResult
    suspend fun sendNotification(receiverId:String, notification:Map<String ,Any>):ApiResult
    suspend fun deleteNotification(receiverId: String,notificationId:String):ApiResult
}