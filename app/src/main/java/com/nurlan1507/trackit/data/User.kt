package com.nurlan1507.trackit.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nurlan1507.trackit.data.notifications.Notification

@Entity
class User(
    @PrimaryKey var uid:String,
    var email:String,
    var username:String,
    @ColumnInfo(name = "device_token")var deviceToken:String,
    var friends:ArrayList<User>?,
    @ColumnInfo(name = "avatar_url") var avatarUrl: String="noAva") {
    constructor(): this("dummmy","DUMMYEMAL","asd", "deviceToken", arrayListOf(),"ava")

    fun <T :User?> withId(id:String):T{
        this.uid = id
        return this as T
    }

}
