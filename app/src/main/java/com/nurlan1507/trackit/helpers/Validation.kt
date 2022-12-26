package com.nurlan1507.trackit.helpers

import android.util.Patterns

class Validation {
    fun isEmpty(field:String):Boolean{
        if(field.isEmpty()){
            return false
        }
        return true
    }

    fun isEmailValid(email:String):Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun checkLength(field:String, minLen:Int):Boolean{
        if(field.length < minLen){
            return false
        }
        return true
    }

}