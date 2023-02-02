package com.nurlan1507.trackit.data

import android.graphics.drawable.Drawable
import com.nurlan1507.trackit.R


class Project(
    var id:String, var title:String, var description:String, var image:ProjectBackGround,var members:MutableList<String>,  var startDate:Long, var endDate:Long, var admins: MutableList<String>) {
    constructor(): this("id", "dummmy","DUMMY", ProjectBackGround(), mutableListOf(), System.currentTimeMillis(),System.currentTimeMillis(), mutableListOf())

    fun <T :Project?> withId(id:String):T{
        this.id = id
        return this as T
    }

}

class User_Project(
    var userId:String,
    var projectId:String
){
    constructor():this("asd","ASD")
}
