package com.nurlan1507.trackit.viewmodels

import android.icu.util.LocaleData
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.nurlan1507.trackit.data.Project
import com.nurlan1507.trackit.data.User
import com.nurlan1507.trackit.helpers.ApiFailure
import com.nurlan1507.trackit.helpers.ApiSuccess
import com.nurlan1507.trackit.repositories.ProjectRepo
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

class ProjectViewModel:ViewModel() {
    //repository
    private val projectRepository = ProjectRepo.projectRepo

    //list of projects
    private var _projects:MutableLiveData<List<Project>> = MutableLiveData<List<Project>>()
    val projects:LiveData<List<Project>> = _projects
    //project object that is going to be created
    private var _project:MutableLiveData<Project> = MutableLiveData<Project>(Project())
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

    fun setBackground(imgId:Int){
        _project.value?.image?.id = imgId
    }



    fun setProjectData(title:String, description: String, adminUser:User, listener:()->Boolean){
        try{
            _project.value?.title = title
            _project.value?.description = description
            _project.value?.admins = mutableListOf(adminUser)
            listener()
        }catch (e:Exception){
            Log.d("Err", e.message.toString())
        }


    }
    fun addUser(userId: String):Boolean{
        try{
            _project.value?.members?.add(userId)
            return true
        }catch (e:Exception) {
            return false
        }
    }
    fun removeUser(userId: String):Boolean{
        try{
            _project.value?.members?.remove(userId)
            return true
        }catch (e:Exception) {
            return false
        }
    }

    fun createProject(listener: (project:Project) -> Unit){
        viewModelScope.launch {
            if(_project.value != null){
                var result = projectRepository.createProject(_project.value!!)
                if(result is ApiSuccess){
                    projectRepository.addAdminJunction((result.list as Project).admins[0].uid, (result.list as Project).id)
                    listener(result.list as Project)
                }else{

                }
            }else{
                return@launch
            }
        }
    }

    fun getProjects(userId:String){
        viewModelScope.launch {
            val projectRes = projectRepository.getProject(userId)
            if(projectRes is ApiSuccess){
                _projects.value = (projectRes.list as List<Project>)
            }else if(projectRes is ApiFailure){
                Log.d("getProjects", projectRes.e.message.toString())
            }
        }
    }
}