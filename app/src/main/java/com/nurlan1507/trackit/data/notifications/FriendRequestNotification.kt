package com.nurlan1507.trackit.data.notifications


//notificationId и receiverId это тоже самое

class FriendRequestNotification
    (notificationId:String ,text:String, var senderId:String):Notification(notificationId, text) {
        constructor():this("id","this is notification", "senderID")
}