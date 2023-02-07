package com.nurlan1507.trackit.repositories

import com.nurlan1507.trackit.data.Task
import com.nurlan1507.trackit.helpers.ApiResult

interface ITaskRepository {
    suspend fun createTask(projectId:String, task: Task):ApiResult
    suspend fun getTasks(projectId:String):ApiResult
}