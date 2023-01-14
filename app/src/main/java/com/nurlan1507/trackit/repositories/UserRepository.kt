package com.nurlan1507.trackit.repositories

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.nurlan1507.trackit.MainActivity
import com.nurlan1507.trackit.data.User
import com.nurlan1507.trackit.data.notifications.FriendRequestNotification
import com.nurlan1507.trackit.helpers.ApiFailure
import com.nurlan1507.trackit.helpers.ApiResult
import com.nurlan1507.trackit.helpers.ApiSuccess
import com.nurlan1507.trackit.utils.NotificationWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await


class UserRepository() :IUserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val userCollection = db.collection("users")
//
//    init{
//        userCollection.document(FirebaseAuth.getInstance().currentUser?.uid.toString())
//            .collection("notication")
//            .addSnapshotListener(MetadataChanges.INCLUDE){snapshot, e ->
//                if(e != null){
//                    Log.w("FirebaseError", e.message.toString())
//                }
//            //Запустить уведомление, но класс требует контекста
//            }
//
//
//    }

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
            val notification = FriendRequestNotification(receiver.uid,"${sender.username} (${sender.email}) wants to be friends with you", sender.uid)
            NotificationRepo.notificationRepository.sendNotification(receiver.uid,notification)


            db.collection("users").document(receiver.uid)
                .collection("friends").document(sender.uid)
                .set(mapOf("friend" to false)).await()


            ApiSuccess()
        }catch (e:Exception){
            ApiFailure(e)
        }
    }

    override suspend fun respondToFriendRequest(responderId: String) {

    }
}

object UserRepo{
    val userRepository = UserRepository()

}
