package com.nurlan1507.trackit.data

class Task(var taskId:String,var title:String, var description:String,var img:String, var startDate:Long, var endDate:Long? ,var users:List<User>) {
    constructor():this("","","s","s",System.currentTimeMillis(),null, listOf())

    fun <T :Task?> withId(id:String):T{
        this.taskId = id
        return this as T
    }
}
