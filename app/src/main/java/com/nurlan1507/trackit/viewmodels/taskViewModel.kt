package com.nurlan1507.trackit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nurlan1507.trackit.data.Task

class TaskViewModel:ViewModel(){
    private var _tasks:MutableLiveData<List<Task>> = MutableLiveData<List<Task>>(listOf())
    val tasks:LiveData<List<Task>> = _tasks


    fun createTask(title:String, description:String){

    }
}