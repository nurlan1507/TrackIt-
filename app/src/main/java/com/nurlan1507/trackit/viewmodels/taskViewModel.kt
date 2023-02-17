package com.nurlan1507.trackit.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nurlan1507.trackit.data.Task
import com.nurlan1507.trackit.data.User
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class TaskViewModel:ViewModel(){
    val zero = 0
    private var _tasks:MutableLiveData<List<Task>> = MutableLiveData<List<Task>>(listOf())
    val tasks:LiveData<List<Task>> = _tasks

    private var currentDate = LocalDate.now()
    private var dayOfYear = currentDate.dayOfYear
    private val dateTime =  LocalDateTime.of(currentDate.year,currentDate.month,currentDate.dayOfMonth,0,0)
    private val currentDateTimeStamp = dateTime.toEpochSecond(java.time.ZoneOffset.UTC)

    private var _task:MutableLiveData<Task> = MutableLiveData(null)
    val task:LiveData<Task> = _task

    private var _startDate:MutableLiveData<Long?> = MutableLiveData<Long?>(currentDateTimeStamp)

    private var _endDate:MutableLiveData<Long?> = MutableLiveData<Long?>(0)
    val endDate:LiveData<Long?> = _startDate

    var formatterInstance = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
    var timeFormatterInstance = DateTimeFormatter.ofPattern("hh:mm")

    init{
        formatterInstance.timeZone = TimeZone.getTimeZone("UTC")
        Log.d("dayofyear", currentDate.dayOfYear.toString())
        Log.d("timestamp", currentDateTimeStamp.toString())
        Log.d("Current date", formatterInstance.format(Date(currentDateTimeStamp*1000)))
    }
    fun startDate():String{
        return if(_startDate.value != null) {
            var date = Date(_startDate.value!!)
            formatterInstance.format(date)
        } else formatterInstance.format(Date(System.currentTimeMillis()))
    }
    fun setStartDate(timeStamp:Long, time:String){
        val localDateTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
        val timeTimeStamp = localDateTime.toNanoOfDay()/1000000
        _startDate.value = _startDate.value?.minus(timeTimeStamp)?.let { _startDate.value?.minus(it) }
        _startDate.value = timeStamp
    }
    fun setStartTime(timeString:CharSequence){
        val localDateTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"))
        _startDate.value = _startDate.value?.plus(localDateTime.toNanoOfDay()/1000000)
    }

    fun setEndDate(timeStamp:Long, time:String){
        val localDateTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
        val timeTimeStamp = localDateTime.toNanoOfDay()/1000000
        if(endDate.value!=null && endDate.value != zero.toLong()){
            _endDate.value = _endDate.value?.minus(timeTimeStamp)?.let { _endDate.value?.minus(it) }
            return
        }
        _endDate.value = timeStamp
    }
    fun setEndTime(timeString:String){
        val localDateTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"))
        _endDate.value = _startDate.value?.plus(localDateTime.toNanoOfDay()/1000000)

//        val startTime = LocalDateTime.parse(timeString, timeFormatterInstance)
//        val timeStamp = startTime.toEpochSecond(java.time.ZoneOffset.UTC) * 1000
//        _startDate.value = _startDate.value?.plus(timeStamp)
    }


    fun endDate():String{
        val maxHour = 84924000
        return if(_endDate.value !=null || _endDate.value!! > maxHour.toLong()) {
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