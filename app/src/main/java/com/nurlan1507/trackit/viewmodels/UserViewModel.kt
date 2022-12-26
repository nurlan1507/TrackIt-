package com.nurlan1507.trackit.viewmodels

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.util.Patterns
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.data.User
import com.nurlan1507.trackit.helpers.Validation
import com.nurlan1507.trackit.repositories.AuthRepository

const val  passwordVal = ("^"+  "(?=.*[0-9])" + "(?=.*[a-zA-Z])" +"(?=\\S+$)" + ".{6,}" +"$")

class UserViewModel:ViewModel() {
    private var repository:AuthRepository = AuthRepository()
    private var _user = MutableLiveData<User>(User())
    val user:LiveData<User> =_user
    private fun setUser(newUser:User){
        _user.value=newUser
    }

    fun register(
        email:TextInputEditText,
        username:TextInputEditText,
        password:TextInputEditText,
        confirmPassword:TextInputEditText
    ):Boolean{
        if(!validateEmail(email))return false
        if(!validateUsername(username))return false
        if(!validatePassword(password,confirmPassword))return false
        repository.emailPasswordSignUp(email,username,password)
        return true
    }






    private fun validateEmail(email: TextInputEditText):Boolean{
        if(Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches() == false){
            email.error ="The email address is badly formatted."

            email.requestFocus()
            return false
        }
        if(email.text.toString().isEmpty()){
            email.error = "Please fill that form"
            email.requestFocus()
            return false
        }
        return true
    }

    private fun validateUsername(username: TextInputEditText):Boolean{
        if(username.text.toString().isEmpty()){
            username.error = "Please fill that form"
            username.requestFocus()
            return false
        }
        if(username.text.toString().length >= 30 ){
            username.error = "Username can not be more than 30 characters"
            username.requestFocus()
            return false
        }
        if(username.text.toString().length<6){
            username.error = "Username should contain at least 6 elements"
            username.requestFocus()
            return false
        }
        return true
    }


    private fun validatePassword(password:TextInputEditText, confirmPassword: TextInputEditText):Boolean{
        val passwordString = password.text.toString()
        if(passwordString.isEmpty()){
            password.error = "Password must be longer than 6 elements and contain al least one digit"
            password.requestFocus()
            return false
        }
        if(!passwordString.matches(passwordVal.toRegex())){
            password.error ="Password must be longer than 6 elements and contain al least one digit"
            password.requestFocus()
            return false
        }
        if(passwordString != confirmPassword.text.toString()){
            password.error ="Passwords do not match"
            password.requestFocus()
            return false
        }
        return true
    }

}