package com.nurlan1507.trackit.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.nurlan1507.trackit.repositories.UserRepo

class FriendWorker(val ctx: Context, val params:WorkerParameters): CoroutineWorker(ctx,params) {
    override suspend fun doWork(): Result {
        UserRepo.userRepository.addFriendToSender(inputData.getString("senderId").toString(), inputData.getString("receiverId").toString())
        return Result.success()
    }
}