package com.nurlan1507.trackit.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

//class DateAdapter(private var ctx: Context,var date:Long):ArrayAdapter<String>(ctx,android.R.layout.simple_spinner_item, date) {
//    var dateTimeStamp:Long? = null
//    var formatterInstance = SimpleDateFormat("dd MMM", Locale.ENGLISH)
//
//    init{
//        formatterInstance.timeZone = TimeZone.getTimeZone("UTC")
//    }
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val currentTime = System.currentTimeMillis()
//        val date = formatterInstance.format(Date(currentTime))
//        Log.d("datadate", date.toString())
//        val view = TextView(ctx)
//        view.text = date
//        return view
//
//    }
//}