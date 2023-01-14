package com.nurlan1507.trackit.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nurlan1507.trackit.data.User
import com.nurlan1507.trackit.databinding.UserListItemBinding

class UserAdapter(var  onUserClickListener:OnUserClickListener): ListAdapter<User, UserAdapter.UserViewHolder>(Comparator) {
    class UserViewHolder(val binding: UserListItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(user:User, clickListener: OnUserClickListener){
            binding.addToFriendBtn.setOnClickListener {
                clickListener.clickListener(it,user)
            }
            binding.email.text = user.email
            binding.username.text = user.username
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(UserListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = getItem(position)
        holder.bind(currentUser, onUserClickListener)
    }

    companion object Comparator:DiffUtil.ItemCallback<User>(){
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.email == newItem.email
        }

    }
}


class OnUserClickListener(val clickListener:(view:View, user:User)->Unit){}