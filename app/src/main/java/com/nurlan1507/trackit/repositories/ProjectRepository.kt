package com.nurlan1507.trackit.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.nurlan1507.trackit.data.Project
import com.nurlan1507.trackit.data.User
import com.nurlan1507.trackit.data.User_Project
import com.nurlan1507.trackit.helpers.ApiFailure
import com.nurlan1507.trackit.helpers.ApiResult
import com.nurlan1507.trackit.helpers.ApiSuccess
import kotlinx.coroutines.tasks.await

class ProjectRepository:IProjectInterface {
    private val projectCollection = FirebaseFirestore.getInstance().collection("projects")
    private val junctionCollection = FirebaseFirestore.getInstance().collection("junction_user_project")
    private val userCollection = FirebaseFirestore.getInstance().collection("users")
    private val mAuth = FirebaseAuth.getInstance()
    override suspend fun createProject(project: Project): ApiResult {
        return try {
            val mapOfData = mapOf<String, Any>(
                "title" to project.title,
                "description" to project.description,
                "projectImg" to project.image,
                "members" to project.members,
                "startDate" to project.startDate,
                "endDate" to project.endDate,
                "admins" to listOf(mAuth.currentUser?.uid)
            )
            Log.d("LOSHARA", (project.endDate == project.startDate).toString())
            var res = projectCollection.add(mapOfData).await()
            project.id = res.id
            Log.d("newProjectId", res.id)
            ApiSuccess(project)
        } catch (e: Exception) {
            ApiFailure(e)
        }
    }

    override suspend fun addAdminJunction(userId: String,projectId:String): ApiResult {
        return try{
            val bridgeTable = User_Project(userId,projectId)
            junctionCollection.document(userId+"_"+projectId).set(bridgeTable).await()
            ApiSuccess()
        }catch (e:Exception){
            ApiFailure(e)
        }
    }

    override suspend fun addUserJunction(userId: String, projectId: String): ApiResult {
        return try{
            val bridgeTable = User_Project(userId,projectId)
            junctionCollection.document(userId+"_"+projectId).set(bridgeTable).await()
            ApiSuccess()
        }catch (e:Exception){
            ApiFailure(e)
        }
    }


    override suspend fun getProject(id: String): ApiResult {
        return ApiSuccess()
    }

    override suspend fun   getProjects(userId: String): ApiResult {
        var result:ApiResult
        try{
            val bridgeTableDocs =  junctionCollection.whereEqualTo("userId", userId).get().await()
            val projects = mutableListOf<Project>()
            for(document in bridgeTableDocs.documents){
                val project = projectCollection.document(document.get("projectId") as String).get().await()
                projects.add(project.toObject(Project::class.java)!!.withId(project.id))
            }
            Log.d("SADDS","SSSS")
            result = ApiSuccess(projects)
            return result
        }catch (e:Exception){
            Log.d("EEE",e.message.toString())
            result = ApiFailure(e)
            return result
        }
    }

    override suspend fun getProjectMembers(projectId: String): ApiResult {
        return try{
            var i = 0
            Log.d("projectId",projectId)
            var usersMap =  junctionCollection.whereEqualTo("projectId", projectId).get().await()
            val userList:MutableList<User> = mutableListOf()
            for(doc in usersMap.documents){
                Log.d("userEmail","SS")
                var user = userCollection.document(doc.get("userId") as String).get().await()
                userList.add(user.toObject(User::class.java)!!)
                Log.d("userEmail", userList[i].email)
            }
            ApiSuccess(userList)
        }catch (e:Exception){
            ApiFailure(e)
        }
    }
}

object ProjectRepo{
    val projectRepo = ProjectRepository()
}