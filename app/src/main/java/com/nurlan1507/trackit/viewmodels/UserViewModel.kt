package com.nurlan1507.trackit.viewmodels
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.nurlan1507.trackit.data.User
import com.nurlan1507.trackit.repositories.AuthRepository
import com.nurlan1507.trackit.repositories.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

const val  passwordVal = ("^"+  "(?=.*[0-9])" + "(?=.*[a-zA-Z])" +"(?=\\S+$)" + ".{6,}" +"$")

class UserViewModel:ViewModel() {
    private var repository:AuthRepository = AuthRepository()
    private var _user = MutableLiveData<User?>()
    private var _isAuth:MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private var _test:MutableLiveData<String> = MutableLiveData<String>()
    val isAuth:Boolean = _isAuth.value == true


    val user:LiveData<User?> =_user
    private fun setUser(newUser:User?){
        _user.value=newUser
    }

    init{
        viewModelScope.launch {
            val res = repository.getCurrentUser()
            if(res is Success){
                setUser(res.user)
            }else if(res is Failure){
                setUser(null)
            }
        }
    }


    fun login(email: String, password: String, listener:(Result)->Unit) {
        viewModelScope.launch {
            val result = repository.emailPasswordSignIn(email, password)
            listener(result!!)
        }
    }


    fun register(email: String, username: String, password: String, listener: (Result) -> Unit){
        viewModelScope.launch {
            val result = repository.emailPasswordSignUp(email,username,password)
            listener(result!!)

            }
        }


    suspend fun googleOauth(uid:String?, account:GoogleSignInAccount) {
        viewModelScope.launch {
            if (uid != null) {
                val result = repository.googleAuth(uid, account)
                if (result is Success) {
                    _user.value = result.user
                    _isAuth.value = true
                }
            } else {
                _isAuth.value = false
                return@launch
            }
        }
    }

    fun logout(){
        repository.logout()
        _isAuth.value = false
    }
}