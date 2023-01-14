package com.nurlan1507.trackit.data.notifications
//notificationId и receiverId это тоже самое

open class Notification(var notificationId:String, var text:String):INotification {
    constructor(): this("dummmy","DUMMYEMAL")
}