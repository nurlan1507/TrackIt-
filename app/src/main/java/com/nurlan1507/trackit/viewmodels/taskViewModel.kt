package com.nurlan1507.trackit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nurlan1507.trackit.data.Task
import com.nurlan1507.trackit.data.User
import java.text.SimpleDateFormat
import java.util.*

class TaskViewModel:ViewModel(){
    private var _tasks:MutableLiveData<List<Task>> = MutableLiveData<List<Task>>(listOf())
    val tasks:LiveData<List<Task>> = _tasks

    private var _task:MutableLiveData<Task> = MutableLiveData(null)
    val task:LiveData<Task> = _task

    private var _startDate:MutableLiveData<Long?> = MutableLiveData<Long?>(System.currentTimeMillis())
    private var _endDate:MutableLiveData<Long?> = MutableLiveData<Long?>(null)
    val endDate:LiveData<Long?> = _startDate

    var formatterInstance = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
    init{
        formatterInstance.timeZone = TimeZone.getTimeZone("UTC")
    }
    fun startDate():String{
        return if(_startDate.value != null) {
            var date = Date(_startDate.value!!)
            formatterInstance.format(date)
        } else formatterInstance.format(Date(System.currentTimeMillis()))
    }
    fun setStartDate(timeStamp:Long){
        _startDate.value = timeStamp
    }
    fun setEndDate(timeStamp: Long){
        _endDate.value = timeStamp
    }
    fun endDate():String{
        return if(_endDate.value !=null) {
            formatterInstance.format(Date(_endDate.value!!))
        } else "No date"
    }

    fun validateDates():Boolean{
        return _startDate.value!! <= _endDate.value!!
    }

    fun createTask(title:String, description:String, userList:List<User>){
        _task.value?.title = title
        _task.value?.description = description
        _task.value?.users = userList
        //repository

    }

    fun initiateTask(){
        _task.value = Task()
    }


}