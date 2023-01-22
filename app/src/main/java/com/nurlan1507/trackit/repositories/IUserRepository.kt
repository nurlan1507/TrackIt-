package com.nurlan1507.trackit.repositories

import com.nurlan1507.trackit.data.User
import com.nurlan1507.trackit.helpers.ApiResult

interface IUserRepository {
    suspend fun findUsers(username:String):List<User>
    suspend fun sendFriendRequest(sender:User, receiver:User):ApiResult
    suspend fun respondToFriendRequest(responderId:String)
    suspend fun getFriends(userId:String):ApiResult
    suspend fun getPendingFriends(userId:String):ApiResult
    suspend fun getUser(userId:String):ApiResult
}