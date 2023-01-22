package com.nurlan1507.trackit.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(val context: Context, val params:WorkerParameters):Worker(context,params) {
    override fun doWork(): Result {
        NotificationSender.notificationSender.sendMessage(inputData.getString("token")!!,inputData.getString("message")!!)
        return Result.success()
    }
}