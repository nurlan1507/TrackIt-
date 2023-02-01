package com.nurlan1507.trackit.repositories

import com.nurlan1507.trackit.data.Project
import com.nurlan1507.trackit.helpers.ApiResult

interface IProjectInterface {
    suspend fun createProject(project:Project):ApiResult
    suspend fun getProject(id:String):ApiResult
}