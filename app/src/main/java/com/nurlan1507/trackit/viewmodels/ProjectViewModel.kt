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

    private var _startDate:MutableLiveData<Long?> = MutableLiveData<Long?>(null)
    private var _endDate:MutableLiveData<Long?> = MutableLiveData<Long?>(null)



    @RequiresApi(Build.VERSION_CODES.O)
    val formatter = DateTimeFormatter.ofPattern("MMM, dd yyyy")

    @RequiresApi(Build.VERSION_CODES.O)
    fun startDate():String{
        return if(_startDate.value != null) {
            Instant.ofEpochSecond(_startDate.value!!).atZone(ZoneId.systemDefault()).format(formatter)
        } else Instant.ofEpochSecond(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)).atZone(ZoneId.systemDefault()).format(formatter)
    }
    fun setStartDate(timeStamp:Long){
        _startDate.value = timeStamp
    }
    fun setEndDate(timeStamp: Long){
        _endDate.value = timeStamp
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun endDate():String{
        return if(_endDate.value !=null) {
            Instant.ofEpochSecond(_endDate.value!!).atZone(ZoneId.systemDefault()).format(formatter)
        } else "No date"
    }





    fun createProject(newProject:Project){
        //TODO validation


        _project.value = newProject
    }

}