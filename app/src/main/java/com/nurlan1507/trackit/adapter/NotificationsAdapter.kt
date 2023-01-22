package com.nurlan1507.trackit.adapter

import android.app.Activity
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.api.Context
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.data.notifications.INotification
import com.nurlan1507.trackit.data.notifications.Notification
import com.nurlan1507.trackit.databinding.ItemNotificationBinding

import java.text.SimpleDateFormat
import java.util.*

class NotificationsAdapter(var context: android.content.Context, val FriendRequestNotificationListener:(notification:INotification)->Unit):ListAdapter<Notification,NotificationsAdapter.NotificationViewHolder>(diffUtil) {
    private lateinit var binding:ItemNotificationBinding

    var formatterInstance = SimpleDateFormat("MMM, dd yyyy", Locale.getDefault())
    init{
        formatterInstance.timeZone = TimeZone.getTimeZone("UTC")
    }


    fun getDate(timeStamp :Long):String{
        val date = Date(timeStamp)
        return formatterInstance.format(date)
    }


    inner class NotificationViewHolder(var binding:ItemNotificationBinding):ViewHolder(binding.root){
        fun bind(notification:Notification) {
            binding.root.setOnClickListener {
                FriendRequestNotificationListener(notification)
            }
            binding.notificationText.text = notification.text
            binding.notificationDate.text = getDate(notification.date)
//            Picasso.get().load(notification.sender.avatarUrl).into(binding.userAvatar)
//            binding.userAvatar.setImageURI(notification.sender.avatarUrl.toUri())
//            var inputStream = URL(notification.sender.avatarUrl).openStream()
//            var bmp = BitmapFactory.decodeStream(inputStream)
//            binding.userAvatar.setImageBitmap(bmp)
            Glide.with(context).load(notification.sender.avatarUrl).placeholder(R.drawable.avatar_shape).into(binding.userAvatar)

        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotificationViewHolder(binding)
    }


    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object diffUtil:DiffUtil.ItemCallback<Notification>(){
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.notificationId == newItem.notificationId
        }

    }

}