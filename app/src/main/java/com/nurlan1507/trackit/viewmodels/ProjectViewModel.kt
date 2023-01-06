package com.nurlan1507.trackit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nurlan1507.trackit.data.Project

class ProjectViewModel:ViewModel() {
    private var _projects:MutableLiveData<List<Project>> = MutableLiveData<List<Project>>()
    val projects:LiveData<List<Project>> = _projects



}