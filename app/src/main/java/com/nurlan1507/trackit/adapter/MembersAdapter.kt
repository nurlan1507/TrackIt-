package com.nurlan1507.trackit.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.api.Distribution.BucketOptions.Linear
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.data.User
import com.nurlan1507.trackit.databinding.FriendListItemBinding
import com.nurlan1507.trackit.databinding.MemberListItemBinding
import org.w3c.dom.Text

//class MembersAdapter(val members:List<User>):RecyclerView.Adapter<MembersAdapter.MemberViewHolder>() {
//    inner class MemberViewHolder(var binding: FriendListItemBinding):RecyclerView.ViewHolder(binding.root){
//        fun bind(member:User){
//            binding.email.text = member.email
//            binding.username.text = member.username
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
//        val binding = FriendListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
//        return MemberViewHolder(binding)
//    }
//
//    override fun getItemCount(): Int {
//        return members.size
//    }
//
//    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
//        holder.bind(members.get(position))
//    }
//}
@SuppressLint("ResourceType")
class MembersAdapter(val ctx: Context, var members:List<User>, val onClickListener:(view:View)->Boolean):ArrayAdapter<User>(ctx,android.R.layout.simple_spinner_item,members){
    val pickedItems:MutableList<User> = mutableListOf()
    inner class MemberViewHolder(var binding: MemberListItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(member:User){
            var imageView = binding.memberAvatar
            if(pickedItems.contains(member)){
                binding.memberSelected.isChecked = true
            }
            binding.memberEmail.text = member.email
            binding.memberUsername.text = member.username
            Glide.with(context).load(member.avatarUrl).into(imageView)
            binding.root.setOnClickListener {
                val isIn = onClickListener(it)
                if(isIn==false){
                    pickedItems.add(member)
                }else{
                    pickedItems.remove(member)
                }
            }
        }
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val user = members.get(position)

        val textView = TextView(context)
        textView.text = "Select a user"
        return textView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding =MemberListItemBinding.inflate(LayoutInflater.from(context), null,false)
        val viewHolder = MemberViewHolder(binding)
        viewHolder.bind(members.get(position))
        return viewHolder.itemView
    }

    override fun getItem(position: Int): User? {
        notifyDataSetChanged()
        return super.getItem(position)
    }
}