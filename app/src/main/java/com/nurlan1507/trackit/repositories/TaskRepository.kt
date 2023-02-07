package com.nurlan1507.trackit.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.nurlan1507.trackit.data.Task
import com.nurlan1507.trackit.helpers.ApiFailure
import com.nurlan1507.trackit.helpers.ApiResult
import com.nurlan1507.trackit.helpers.ApiSuccess
import kotlinx.coroutines.tasks.await

class TaskRepository:ITaskRepository {
    private val db = FirebaseFirestore.getInstance()
    override suspend fun createTask(projectId:String, task: Task): ApiResult {
        return try{
            val taskMap = mapOf<String,Any>(
                "title" to task.title,
                "description" to task.description,
                "startDate" to task.startDate,
                "endDate" to task.endDate,
            )
            db.collection("projects").document(projectId).collection("tasks").add(taskMap).await()
            //add users with worker
            ApiSuccess()
        }catch (e:Exception){
            ApiFailure(e)
        }
    }

    override suspend fun getTasks(projectId:String): ApiResult {
        return try{
            val result = db.collection("projects").document(projectId).collection("tasks").get().await()
            val tasks = mutableListOf<Task>()
            for(doc in result.documents){
                val taskObj = doc.toObject(Task::class.java)!!
            }
            ApiSuccess(tasks)
        }catch (e:Exception){
            ApiFailure(e)
        }
    }


}