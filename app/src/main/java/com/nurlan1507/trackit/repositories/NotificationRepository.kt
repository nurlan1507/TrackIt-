package com.nurlan1507.trackit.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nurlan1507.trackit.data.notifications.FriendRequestNotification
import com.nurlan1507.trackit.data.notifications.Notification
import com.nurlan1507.trackit.helpers.ApiFailure
import com.nurlan1507.trackit.helpers.ApiResult
import com.nurlan1507.trackit.helpers.ApiSuccess
import kotlinx.coroutines.tasks.await

class NotificationRepository:INotificationRepository {
    private val db = FirebaseFirestore.getInstance().collection("users")
    override suspend fun getNotifications(): List<Notification> {
        TODO("Not yet implemented")
    }

    override suspend fun sendNotification(receiverId:String, notification: FriendRequestNotification):ApiResult {
        return try{
            db.document(receiverId).collection("notication").document(FirebaseAuth.getInstance().currentUser!!.uid).set(notification).await()
            ApiSuccess()
        } catch (e:Exception){
            ApiFailure(e)
        }
    }




}


object NotificationRepo{
    val notificationRepository = NotificationRepository()
}