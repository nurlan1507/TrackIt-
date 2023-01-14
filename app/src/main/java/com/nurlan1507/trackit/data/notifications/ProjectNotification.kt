package com.nurlan1507.trackit.data.notifications

class ProjectNotification
    (notificationId:String ,text:String, var projectId:String):Notification(notificationId, text){
        constructor():this("id","this is notification", "projectId")
}