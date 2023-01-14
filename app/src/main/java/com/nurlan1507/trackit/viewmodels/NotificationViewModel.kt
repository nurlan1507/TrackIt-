package com.nurlan1507.trackit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nurlan1507.trackit.data.notifications.Notification

class NotificationViewModel:ViewModel() {
    private var _notifications:MutableLiveData<List<Notification>> = MutableLiveData<List<Notification>>()
    val notification get():LiveData<List<Notification>> = _notifications
    fun setNotifications(notifications: List<Notification>){
        _notifications.value = notifications
    }




}