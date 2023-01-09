package com.nurlan1507.trackit.repositories

import com.nurlan1507.trackit.data.User

interface IUserRepository {
    fun findUsers(username:String):List<User>
    fun sendFriendRequest()

}