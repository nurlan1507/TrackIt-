package com.nurlan1507.trackit.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class FriendWorker(val ctx: Context, val params:WorkerParameters):Worker(ctx,params) {
    override fun doWork(): Result {

        return Result.success()
    }
}