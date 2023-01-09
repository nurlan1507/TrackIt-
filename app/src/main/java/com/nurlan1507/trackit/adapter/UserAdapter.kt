package com.nurlan1507.trackit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nurlan1507.trackit.data.User
import com.nurlan1507.trackit.databinding.FragmentUserSearchBinding
import com.nurlan1507.trackit.databinding.UserListItemBinding

class UserAdapter: ListAdapter<User, UserAdapter.UserViewHolder>(Comparator) {
    class UserViewHolder(val binding: UserListItemBinding):RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(UserListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = getItem(position)
        holder.binding
        holder.binding.apply {
            username.text = currentUser.username
            email.text = currentUser.email
        }
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