package com.nurlan1507.trackit.data

class User(var uid:String, var email:String, var username:String,var avatarUrl: String="noAva") {
    constructor(): this("dummmy","DUMMYEMAL","asd")


    fun <T :User?> withId(id:String):T{
        this.uid = id
        return this as T
    }

}
