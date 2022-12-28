package com.nurlan1507.trackit.viewmodels

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputEditText
import com.nurlan1507.trackit.data.User
import com.nurlan1507.trackit.repositories.AuthRepository
import com.nurlan1507.trackit.repositories.*
import kotlinx.coroutines.launch
import kotlin.Error

const val  passwordVal = ("^"+  "(?=.*[0-9])" + "(?=.*[a-zA-Z])" +"(?=\\S+$)" + ".{6,}" +"$")

class UserViewModel:ViewModel() {
    private var repository:AuthRepository = AuthRepository()
    private var _user = MutableLiveData<User?>()
    var _userSignInErrorLiveData:MutableLiveData<String> = MutableLiveData<String>()
    val userSignInErrorLiveData: LiveData<String> = _userSignInErrorLiveData
    private fun setError(err:String){
        _userSignInErrorLiveData.value = err
    }

    val user:LiveData<User?> =_user
    private fun setUser(newUser:User){
        _user.value=newUser
    }
    init{
        val res = repository.getCurrentUser()
        if(res is Success){
            _user.value = res.user
        }else if(res is Failure){
            setError(res.error)
        }
    }



    suspend fun login(email:String, password: String){
        viewModelScope.launch(){
            val result = repository.emailPasswordSignIn(email,password)
            if(result == null){
                Log.d("LOX", "ебать ты лох братуха")
            }
            if(result is Success){
                setUser(result.user)
                Log.d("VIEMODELAUTH","${_user.value?.username.toString()} $_user.value?.email.toString() ok")
            }else if(result is Failure){
                setError(result.error)
                Log.d("ERROR_AUTH", _userSignInErrorLiveData.value.toString())
            }
        }

    }

    suspend fun register(email: String, username: String, password: String, repeatPassword:String):Boolean{
        val result = repository.emailPasswordSignUp(email,username,password)
        if(result is Success){
            _user.value = result.user
            return true
        }else if(result is Failure){
            _userSignInErrorLiveData.value = result.error
        }
        return false
    }












}