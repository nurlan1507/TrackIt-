package com.nurlan1507.trackit.utils

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class NotificationSender {
    private val FCM_URL = "https://fcm.googleapis.com/fcm/send"
    private val key = "AAAA7iAKwzk:APA91bHNpuKA3nsS0zVcv2OQAfEgvU4nUYk0Ct8UFzZ2hV6DOwe1H4jsu0QCgJZK_4lxVZ0g4WWtTorCA3MY2l9139DK5OEMe05KLIZNZvS4NdmJZ5aft5Odm7-PCf9RW7Fsz7_VIkYW"

    val okHttpClient = OkHttpClient()


    fun sendMessage(fcmToken:String, text:String ){
        val message = JSONObject()
        val json = JSONObject()
        val notificationJson = JSONObject()
        val dataJson = JSONObject()
        notificationJson.put("body", text)
        notificationJson.put("title", "A new friend request")
        notificationJson.put("priority", "high")
        dataJson.put("customId", "02")
        dataJson.put("badge", 1)
        dataJson.put("alert", "Alert")
        json.put("notification", notificationJson)
        json.put("data", dataJson)
        json.put("to", fcmToken)
        val reqBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json.toString())
        val request = Request.Builder()
            .url(FCM_URL)
            .addHeader("Content-type","application/json")
            .addHeader("Authorization", "key=$key")
            .post(reqBody)
            .build()
        Log.d("FMCTOKEN", FirebaseMessaging.getInstance().token.toString())
        val call= okHttpClient.newCall(request)
        call.enqueue(callback())
        call.execute()
    }

    companion object{
        val notificationSender = NotificationSender()
    }

    class callback:Callback{
        override fun onFailure(call: Call, e: IOException) {
        }
        override fun onResponse(call: Call, response: Response) {
            Log.d("Worked",response.code.toString())
        }
    }
}