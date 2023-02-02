package com.nurlan1507.trackit.utils

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.nurlan1507.trackit.repositories.ProjectRepo
import com.nurlan1507.trackit.repositories.UserRepo

class ProjectWorker(var ctx: Context, args:WorkerParameters): CoroutineWorker(ctx,args) {
    private val projectRepo = ProjectRepo.projectRepo
    private val userRepo = UserRepo.userRepository
    override suspend fun doWork(): Result {
        val listOfUsers = inputData.getStringArray("memberList")
        val projectId = inputData.getString("projectId")
        if (listOfUsers != null && projectId != null) {
            for(userId in listOfUsers){
                projectRepo.addUserJunction(userId,projectId)
                Log.d("sukablyat","suka")
            }
        }
        return Result.success()
    }
}