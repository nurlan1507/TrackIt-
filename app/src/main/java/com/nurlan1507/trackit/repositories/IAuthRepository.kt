package com.nurlan1507.trackit.repositories

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface IAuthRepository {
    suspend fun emailPasswordSignUp(email:String,username:String, password:String):AuthResult?
    suspend fun emailPasswordSignIn(email: String, password: String):AuthResult?
    suspend fun getCurrentUser():AuthResult?
    suspend fun getUser(uid:String): AuthResult?
    fun logout()
    suspend fun googleAuth(uid:String, account: GoogleSignInAccount):AuthResult?
}