package com.nurlan1507.trackit.repositories

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface IAuthRepository {
    suspend fun emailPasswordSignUp(email:String,username:String, password:String):Result?
    suspend fun emailPasswordSignIn(email: String, password: String):Result?
    suspend fun getCurrentUser():Result?
    suspend fun getUser(uid:String): Result?
    fun logout()
    suspend fun googleAuth(uid:String, account: GoogleSignInAccount):Result?
}