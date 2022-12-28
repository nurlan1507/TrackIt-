package com.nurlan1507.trackit.helpers

import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText
import com.nurlan1507.trackit.viewmodels.passwordVal

fun validateEmail(email: TextInputEditText):Boolean{
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

fun validateUsername(username: TextInputEditText):Boolean{
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


 fun validatePassword(password: TextInputEditText, confirmPassword: TextInputEditText?):Boolean{
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
     if(confirmPassword !=null){
         if(passwordString != confirmPassword.text.toString()){
             password.error ="Passwords do not match"
             password.requestFocus()
             return false
         }
     }

    return true
}