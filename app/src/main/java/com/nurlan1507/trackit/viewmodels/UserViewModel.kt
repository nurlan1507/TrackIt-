package com.nurlan1507.trackit.viewmodels
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.nurlan1507.trackit.data.User
import com.nurlan1507.trackit.repositories.AuthRepository
import com.nurlan1507.trackit.repositories.*
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

const val  passwordVal = ("^"+  "(?=.*[0-9])" + "(?=.*[a-zA-Z])" +"(?=\\S+$)" + ".{6,}" +"$")

class UserViewModel:ViewModel() {
    private var repository:AuthRepository = AuthRepository()
    private var _user = MutableLiveData<User?>()

    private var _userSignUpErrorLiveData:MutableLiveData<String> = MutableLiveData<String>()
    private var _userSignInErrorLiveData:MutableLiveData<String> = MutableLiveData<String>()
    private var _isAuth:MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val userSignInErrorLiveData: LiveData<String> = _userSignInErrorLiveData
    val userSignUpErrorLiveData: LiveData<String> = _userSignUpErrorLiveData
    val isAuth:LiveData<Boolean> = _isAuth

    private fun setLoginError(err:String){
        _userSignInErrorLiveData.value = err
    }
    private fun setRegisterError(err:String){
        _userSignUpErrorLiveData.value = err
    }

    val user:LiveData<User?> =_user
    private fun setUser(newUser:User){
        _user.value=newUser
    }

    init{
        viewModelScope.launch {
            val res = repository.getCurrentUser()
            if(res is Success){
                setUser(res.user)
                _isAuth.value = true
            }else if(res is Failure){
                _isAuth.value = false
            }
           Log.d("KOTAK", FirebaseAuth.getInstance().currentUser?.email.toString())
        }
    }


    suspend fun login(email:String, password: String){
        viewModelScope.launch(){
            val result = repository.emailPasswordSignIn(email,password)
            if(result == null){
                Log.d("LOX", "ебать ты лох братуха")
                return@launch
            }
            if(result is Success){
                setUser(result.user)
                _isAuth.value = true
                Log.d("VIEMODELAUTH","${_user.value?.username.toString()} $_user.value?.email.toString() ok")
            }else if(result is Failure){
                setLoginError(result.error)
                _isAuth.value = false
                Log.d("ERROR_AUTH", _userSignInErrorLiveData.value.toString())
            }
        }
    }

    suspend fun register(email: String, username: String, password: String, repeatPassword:String){
        viewModelScope.launch {
            val result = repository.emailPasswordSignUp(email,username,password)
            if(result is Success){
                setUser(result.user)
                _isAuth.value = true
            }else if(result is Failure){
                setRegisterError(result.error)
                _isAuth.value = false
            }
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