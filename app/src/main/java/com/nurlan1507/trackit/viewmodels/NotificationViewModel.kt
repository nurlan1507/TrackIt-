package com.nurlan1507.trackit.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nurlan1507.trackit.data.notifications.Notification
import com.nurlan1507.trackit.helpers.ApiFailure
import com.nurlan1507.trackit.helpers.ApiSuccess
import com.nurlan1507.trackit.repositories.NotificationRepo
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import java.text.SimpleDateFormat
import java.util.*

class NotificationViewModel:ViewModel() {
    private var _notifications:MutableLiveData<List<Notification>> = MutableLiveData<List<Notification>>(listOf())
    private val repository = NotificationRepo.notificationRepository
    val notifications get():LiveData<List<Notification>> = _notifications
    var formatterInstance = SimpleDateFormat("MMM, dd yyyy", Locale.getDefault())

    init{
        formatterInstance.timeZone = TimeZone.getTimeZone("UTC")
    }

    private fun setNotifications(notifications: List<Notification>) {
        _notifications.value = notifications
    }

    fun getNotifications(){
        viewModelScope.launch {
            val result = repository.getNotifications()
            if(result is ApiSuccess){
                _notifications.value = result.list as List<Notification>
            }else if(result is ApiFailure){
                Log.d("SOSAT", result.e.message.toString())
            }
        }
    }

}