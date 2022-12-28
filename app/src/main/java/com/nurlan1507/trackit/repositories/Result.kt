package com.nurlan1507.trackit.repositories

import androidx.lifecycle.MutableLiveData
import com.nurlan1507.trackit.data.User

abstract class Result {
}

class Success(val user:User):Result(){

}

class Failure(val error:String):Result(){

}