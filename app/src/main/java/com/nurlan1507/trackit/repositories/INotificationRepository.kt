package com.nurlan1507.trackit.repositories

import com.nurlan1507.trackit.data.notifications.FriendRequestNotification
import com.nurlan1507.trackit.data.notifications.Notification
import com.nurlan1507.trackit.helpers.ApiResult


interface INotificationRepository {
    suspend fun getNotifications():List<Notification>
    suspend fun sendNotification(receiverId:String, notification:FriendRequestNotification):ApiResult
}