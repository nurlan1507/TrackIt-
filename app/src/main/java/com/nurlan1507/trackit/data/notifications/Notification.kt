package com.nurlan1507.trackit.data.notifications

import com.nurlan1507.trackit.data.User

//notificationId и receiverId это тоже самое

open class Notification(var notificationId:String, var text:String, var sender: User,var date:Long=System.currentTimeMillis()):INotification {
    constructor(): this("dummmy","DUMMYEMAL",User(),System.currentTimeMillis())
    fun <T :Notification?> withId(id:String):T{
        this.notificationId = id
        return this as T
    }
}