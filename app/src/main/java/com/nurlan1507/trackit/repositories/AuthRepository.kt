package com.nurlan1507.trackit.repositories

import android.util.Log
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.nurlan1507.trackit.data.User
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance().collection("users")



    suspend fun emailPasswordSignUp(email:String,username:String, password:String):Result? {
        var result:Result? = null
        try{
            val authRes = mAuth.createUserWithEmailAndPassword(email,password).await()
            if(authRes.user!= null){
                var newUser = User(email,username)
                db.document(authRes.user!!.uid).set(newUser).await()
                result = Success(newUser)
            }
            Log.d("AUTHRES",authRes.user?.email.toString())
        }catch (e:Exception){
            result = Failure(onError(e))
        }
        return  result
    }

    suspend fun emailPasswordSignIn(email: String, password: String):Result?{
        var result:Result? = null
        try{
            val authResult = mAuth.signInWithEmailAndPassword(email,password).await()
            if(authResult.user !=null){
                result = getUser(authResult.user!!.uid)
            }
        }catch (e: Exception){
            result = Failure(onError(e))
        }
        return result
    }

    fun googleSignIn(){

    }


    private fun onError(e :Exception):String{
        return when(e){
            is FirebaseAuthInvalidUserException ->
                "User does not exist"
            is FirebaseAuthInvalidCredentialsException ->
                "Incorrect password"
            is FirebaseAuthEmailException ->
                "Email is badly formatted"
            is FirebaseAuthWeakPasswordException ->
                "Password is weak must be at least 6 characters and including at least number"
            is FirebaseAuthUserCollisionException ->
                "User already exists"
            else ->
                e.message.toString()
        }
    }

//    fun getCurrentUser():Result?{
//        val id = mAuth.currentUser?.uid
//        var res:Result? = null
//        if (id != null) {
//            db.document(id).get()
//                .addOnSuccessListener {
//                    res = Success(it.toObject(User::class.java)!!)
//                }
//                .addOnFailureListener {
//                    res = Failure("no user found")
//                }
//        }
//        return res
//    }

    private suspend fun getUser(uid:String): Result?{
        var result:Result? = null
            try{
                val authRes =  db.document(uid).get().await()
                if(authRes!=null){
                    result = Success(authRes.toObject<User>()!!)
                }
            }catch (e:Exception){
                result = Failure(onError(e))
            }
        return result
    }


    private fun createUser(uid:String, newUser:Map<String,Any>):Result {
        var result:Result? = null
        db.document(uid).set(newUser)
            .addOnSuccessListener {
                result = Success(User(newUser["email"].toString(),newUser["username"].toString()))
            }
            .addOnFailureListener {
                result = Failure("Could not create user, please try again")
            }
        return result!!
    }


    suspend fun googleAuth(uid:String, account:GoogleSignInAccount):Result?{
        //check if user with that id exists
        var user:User
        var result:Result? = null
        try{
            val userDB = db.document(uid).get().await()
            user = User(account.email!!, account.displayName!!, account.photoUrl.toString())
            if(!userDB.exists()){
                db.document(uid).set(user).await()
            }
            result = Success(user)
        }catch (e:Exception){
            result = Failure(e.message!!)
        }
        return result
    }
}