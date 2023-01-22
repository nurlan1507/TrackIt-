package com.nurlan1507.trackit.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.nurlan1507.trackit.MAuth
import com.nurlan1507.trackit.data.notifications.INotification
import com.nurlan1507.trackit.data.notifications.Notification
import com.nurlan1507.trackit.helpers.ApiFailure
import com.nurlan1507.trackit.helpers.ApiResult
import com.nurlan1507.trackit.helpers.ApiSuccess
import kotlinx.coroutines.tasks.await



class NotificationRepository:INotificationRepository {
    private val db = FirebaseFirestore.getInstance().collection("users")
    override suspend fun getNotifications(): ApiResult {
        var result:ApiResult
        try{
            val repoRes = db.document(MAuth.currentUser?.uid.toString()).collection("notification").get().await()
            var notificationList:MutableList<Notification> = mutableListOf()
            for(document in repoRes.documents){
                notificationList.add(document.toObject(Notification::class.java)!!.withId(document.id))
            }
            result = ApiSuccess(notificationList)
            return result
        }catch (e:Exception){
            result = ApiFailure(e)
            return result
        }
    }

    override suspend fun sendNotification(receiverId:String, notification: INotification):ApiResult {
        return try{
            db.document(receiverId).collection("notification").document().set(notification).await()
            ApiSuccess()
        } catch (e:Exception){
            ApiFailure(e)
        }
    }




}


object NotificationRepo{
    val notificationRepository = NotificationRepository()
}