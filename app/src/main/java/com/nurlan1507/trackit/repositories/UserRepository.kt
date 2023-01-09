package com.nurlan1507.trackit.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.nurlan1507.trackit.data.User

class UserRepository() :IUserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val userCollection = db.collection("users")
    override fun findUsers(username:String): List<User> {
        userCollection.orderBy("email").startAt(username).endAt("${username}\uf8ff").get().addOnCompleteListener {
            if(it.isSuccessful){

            }else{

            }
        }
        return listOf()
    }

    override fun sendFriendRequest() {
        TODO("Not yet implemented")
    }
}