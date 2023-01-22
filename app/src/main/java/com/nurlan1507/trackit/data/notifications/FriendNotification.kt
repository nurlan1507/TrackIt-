package com.nurlan1507.trackit.data.notifications

import com.nurlan1507.trackit.data.User

class FriendNotification(
     notificationId:String, text:String, sender: User, date:Long=System.currentTimeMillis()
):Notification(notificationId,text,sender,date) {
    constructor():this("asd","asd",User(),777)
}