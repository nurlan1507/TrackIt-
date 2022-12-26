package com.nurlan1507.trackit.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.nurlan1507.trackit.data.User
import com.nurlan1507.trackit.helpers.Validation

class AuthRepository {
    private var validation:Validation = Validation()
    private val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    fun emailPasswordSignUp(email:TextInputEditText,username:TextInputEditText, password:TextInputEditText):MutableLiveData<User> {
        val emailStr = email.text.toString()
        val passwordStr = password.text.toString()
        val newUser:MutableLiveData<User> = MutableLiveData<User>()
        mAuth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener {
            if (it.isSuccessful) {
                if(mAuth.currentUser!=null){
                    val uid = mAuth.currentUser?.uid
                    db.collection("users").add(mapOf("email" to mAuth.currentUser!!.email, "username" to mAuth.currentUser!!.displayName))
                }
            }else{
                if(it.exception is FirebaseAuthException){
                    email.error = it.exception?.message
                    email.requestFocus()
                }
            }
        }
        return newUser
    }
}