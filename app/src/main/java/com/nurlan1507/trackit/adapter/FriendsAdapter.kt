package com.nurlan1507.trackit.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.nurlan1507.trackit.data.User
import com.nurlan1507.trackit.databinding.FriendListItemBinding
import com.nurlan1507.trackit.databinding.ItemLetterFriendListBinding
import java.util.*

const val FriendLayout = 0
const val LetterLayout = 1
class FriendsAdapter(val ctx:Context, private val itemList:ArrayList<FriendsAdapterItemClass>):RecyclerView.Adapter<ViewHolder>() {

    init{
        for(ind in 0..itemCount-1){
            if(itemList.get(ind).viewType == 0){
                Log.d("ITEML", itemList.get(ind).user.username.toString())

            }else{
                Log.d("ITEML", itemList.get(ind).letter.toString())

            }
        }
    }

    inner class ViewHolderFriend(val binding: FriendListItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(friend:User){
            binding.email.text = "ASDASDASD"
            binding.username.text = friend.username
        }
    }
    inner class ViewHolderLetter(val binding: ItemLetterFriendListBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(letter:String){
            binding.letter.text = letter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            LetterLayout ->{
                val binding = ItemLetterFriendListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                ViewHolderLetter(binding)
            }
            else ->{
                val binding = FriendListItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
                ViewHolderFriend(binding)
            }

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(itemList.get(position).viewType == 1){
            (holder as ViewHolderLetter).bind(itemList.get(position).letter.toString())

        }else{
            (holder as ViewHolderFriend).bind(itemList.get(position).user)

        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(itemList.get(position).viewType){
            0 ->
                FriendLayout
            1 ->
                LetterLayout
            else ->
                -1
        }
    }

}




class FriendsAdapterItemClass {
    var viewType: Int

    var letter: String? = null

    // public constructor for the first layout
    constructor(viewType: Int, text: String?) {
        this.letter  = text
        this.viewType = viewType
    }

    // Variables for the item of second layout
    lateinit var user:User

    // public constructor for the second layout
    constructor(
        viewType: Int, user:User
    ) {
        this.user = user
        this.viewType = viewType
    }

}

