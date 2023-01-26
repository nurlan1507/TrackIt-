package com.nurlan1507.trackit.repositories

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.nurlan1507.trackit.MAuth
import com.nurlan1507.trackit.data.User
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
                val senderRes = db.document(document.get("notificationId") as String).get().await()
                val sender = senderRes.toObject(User::class.java)
                Log.d("SENDER",sender?.email.toString())
                val newNotification = Notification(MAuth.currentUser?.uid.toString(),
                    document.get("text") as String, sender!!, document.get("date") as Long
                )
                notificationList.add(newNotification)
            }
            result = ApiSuccess(notificationList)
            return result
        }catch (e:Exception){
            result = ApiFailure(e)
            return result
        }
    }

    override suspend fun sendNotification(receiverId:String, map: Map<String,Any>):ApiResult {
        return try{
            db.document(receiverId).collection("notification").document(map.get("notificationId") as String).set(map).await()
            ApiSuccess()
        } catch (e:Exception){
            ApiFailure(e)
        }
    }

    override suspend fun deleteNotification(receiverId:String ,notificationId:String):ApiResult{
        return try {
            db.document(receiverId).collection("notification").document(notificationId).delete().await()
            ApiSuccess()
        }catch (e:Exception){
            ApiFailure(e)
        }
    }


}


object NotificationRepo{
    val notificationRepository = NotificationRepository()
}