package com.nurlan1507.trackit.repositories

import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import com.google.rpc.context.AttributeContext
import com.nurlan1507.trackit.repositories.Result
import io.grpc.internal.SharedResourceHolder
import kotlinx.coroutines.tasks.await

//
//interface IAuthService {
//    fun createAnonymousAccount(onResult:(Throwable?) -> Unit)
//    fun authenticate(email: String, password: String, onResult: (Throwable?) -> Unit)
//    fun linkAccount(email: String, password: String, onResult: (Throwable?) -> Unit)
//}
//
//
//
//class AuthService:IAuthService{
//    override fun createAnonymousAccount(onResult: (Throwable?) -> Unit) {
//        Firebase.auth.signInAnonymously().addOnCompleteListener {
//            onResult(it.exception)
//        }
//    }
//
//    override fun authenticate(email: String, password: String, onResult: (Throwable?) -> Unit) {
//        Firebase.auth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener { onResult(it.exception) }
//    }
//
//    override fun linkAccount(email: String, password: String, onResult: (Throwable?) -> Unit) {
//        val credential = EmailAuthProvider.getCredential(email, password)
//
//        Firebase.auth.currentUser!!.linkWithCredential(credential)
//            .addOnCompleteListener { onResult(it.exception) }
//    }
//}


class FirDatabase(var auth:FirebaseAuth){
    fun login(email:String, password:String):Task<AuthResult>{
        return auth.signInWithEmailAndPassword(email,password)
    }
}

class AuthRepo(private val database: FirDatabase){
    suspend fun login(email: String,password: String): com.nurlan1507.trackit.repositories.Result? {
        return try{
            val response = database.login(email, password)
            val result = response.result
            if(response.isSuccessful){
                Log.d("SUCCESSLOsG","LLLLL")
                Success(com.nurlan1507.trackit.data.User(response.result.user?.email!!,response.result.user?.displayName!!))
            }else {
                Log.d("FAILLOG",response.exception?.message.toString())
                Failure(response.exception?.message.toString())
            }
        }catch (e:Exception){
            Log.d("FAILLOG",e.message.toString())
            Failure(e.message.toString())
        }
    }
}