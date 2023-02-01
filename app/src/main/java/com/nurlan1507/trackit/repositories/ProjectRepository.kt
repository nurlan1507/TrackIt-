package com.nurlan1507.trackit.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.nurlan1507.trackit.data.Project
import com.nurlan1507.trackit.helpers.ApiFailure
import com.nurlan1507.trackit.helpers.ApiResult
import com.nurlan1507.trackit.helpers.ApiSuccess
import kotlinx.coroutines.tasks.await

class ProjectRepository:IProjectInterface {
    private val projectCollection = FirebaseFirestore.getInstance().collection("projects")
    private val mAuth = FirebaseAuth.getInstance()
    override suspend fun createProject(project: Project): ApiResult {
        return try {
            val mapOfData = mapOf<String, Any>(
                "title" to project.title,
                "description" to project.description,
                "projectImg" to project.image,
                "users" to project.members,
                "startDate" to project.startDate,
                "endDate" to project.endDate,
                "admins" to listOf(mAuth.currentUser?.uid)
            )
            var res = projectCollection.add(mapOfData).await()
            project.id = res.id
            Log.d("newProjectId", res.id)
            ApiSuccess(project)
        } catch (e: Exception) {
            ApiFailure(e)
        }
    }

    override suspend fun getProject(id: String): ApiResult {
        TODO("Not yet implemented")
    }


}

object ProjectRepo{
    val projectRepo = ProjectRepository()
}