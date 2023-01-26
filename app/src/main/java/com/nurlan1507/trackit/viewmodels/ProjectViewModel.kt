package com.nurlan1507.trackit.viewmodels

import android.icu.util.LocaleData
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nurlan1507.trackit.data.Project
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

class ProjectViewModel:ViewModel() {
    //list of projects
    private var _projects:MutableLiveData<List<Project>> = MutableLiveData<List<Project>>()
    val projects:LiveData<List<Project>> = _projects
    //project object that is going to be created
    private var _project:MutableLiveData<Project> = MutableLiveData<Project>()
    val project:LiveData<Project> get() = _project

    private var _startDate:MutableLiveData<Long?> = MutableLiveData<Long?>(System.currentTimeMillis())
    private var _endDate:MutableLiveData<Long?> = MutableLiveData<Long?>(null)
    val endDate:LiveData<Long?> = _startDate

    var formatterInstance = SimpleDateFormat("MMM, dd yyyy", Locale.getDefault())
    init{
        formatterInstance.timeZone = TimeZone.getTimeZone("UTC")
    }


    fun startDate():String{
        return if(_startDate.value != null) {
            var date = Date(_startDate.value!! * 1000)
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
            formatterInstance.format(Date(_endDate.value!! * 1000))
        } else "No date"
    }



    fun createProject(title:String, description:String){
        //TODO validation

    }

    fun validateDates():Boolean{

        return _startDate.value!! <= _endDate.value!!
    }

}