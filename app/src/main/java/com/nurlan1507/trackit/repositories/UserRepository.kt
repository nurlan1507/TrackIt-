package com.nurlan1507.trackit.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nurlan1507.trackit.data.User
import com.nurlan1507.trackit.data.notifications.Notification
import com.nurlan1507.trackit.helpers.ApiFailure
import com.nurlan1507.trackit.helpers.ApiResult
import com.nurlan1507.trackit.helpers.ApiSuccess
import com.nurlan1507.trackit.utils.NotificationSender
import kotlinx.coroutines.tasks.await


class UserRepository() :IUserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val userCollection = db.collection("users")

    init{
        Log.d("PushNotification",FirebaseAuth.getInstance().currentUser?.uid.toString())




    }

    override suspend fun findUsers(email:String): List<User> {
        val result = userCollection.orderBy("email").limit(10).startAt(email).endAt("${email}\uf8ff").get().await()
        val userList:MutableList<User> = mutableListOf()
        for(document in result.documents){
            userList.add(document.toObject(User::class.java)!!.withId(document.id))
        }
        return userList
    }

    override suspend fun sendFriendRequest(sender:User,receiver:User):ApiResult {
        return try{
            db.collection("users").document(sender.uid)
                .collection("friends").document(receiver.uid)
                .set(mapOf("friend" to false)).await()

            val notification = Notification(receiver.uid,"${sender.username} (${sender.email}) wants to be friends with you", sender)
            NotificationRepo.notificationRepository.sendNotification(receiver.uid,notification)

            db.collection("users").document(receiver.uid)
                .collection("friends").document(sender.uid)
                .set(mapOf("friend" to false, "user" to receiver)).await()

            NotificationSender.notificationSender.sendMessage(receiver.deviceToken,notification.text)
            ApiSuccess()
        }catch (e:Exception){
            ApiFailure(e)
        }
    }

    override suspend fun respondToFriendRequest(responderId: String) {

    }

    override suspend fun getFriends(userId:String):ApiResult{
        return try{
            val result = userCollection.document(userId).collection("friends").whereEqualTo("friend" , true).get().await()
            ApiSuccess(result.toObjects(User::class.java))
        }catch (e:Exception){
            ApiFailure(e)
        }
    }

    override suspend fun getPendingFriends(userId: String): ApiResult {
        return try{
            val result = userCollection.document(userId).collection("friends").whereEqualTo("friend", false).get().await()
            ApiSuccess(result.toObjects(User::class.java))
        }catch (e:Exception){
            ApiFailure(e)
        }
    }

    override suspend fun getUser(userId: String): ApiResult {
        return try{
            val res = userCollection.document(userId).get().await()
            ApiSuccess(res.toObject(User::class.java))
        }catch (e:Exception){
            ApiFailure(e)
        }
    }
}

object UserRepo{
    val userRepository = UserRepository()

}
