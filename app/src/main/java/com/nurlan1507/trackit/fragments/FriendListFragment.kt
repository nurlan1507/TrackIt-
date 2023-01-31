package com.nurlan1507.trackit.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.adapter.FriendsAdapter
import com.nurlan1507.trackit.adapter.FriendsAdapterItemClass
import com.nurlan1507.trackit.data.Project
import com.nurlan1507.trackit.data.User
import com.nurlan1507.trackit.databinding.FragmentFriendListBinding
import com.nurlan1507.trackit.viewmodels.ProjectViewModel
import com.nurlan1507.trackit.viewmodels.UserViewModel


class FriendListFragment : Fragment() {
    private lateinit var binding:FragmentFriendListBinding
    private val userViewModel: UserViewModel by activityViewModels()
    private val projectViewModel:ProjectViewModel by activityViewModels()
    private lateinit var friendList:RecyclerView
    private var friends:MutableList<User> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        friends = userViewModel._friends.value!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendListBinding.inflate(inflater,container,false)
        binding.apply {
            viewLifecycleOwner
        }
        friendList = binding.listUsers
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        friends.sortWith(Comparator {o1:User, o2:User ->
            o1.username.compareTo(o2.username)
        })
        val itemList:ArrayList<FriendsAdapterItemClass> = arrayListOf()

//        var firstLetter = friends[0].username.get(0)
//        itemList.add(FriendsAdapterItemClass(1,firstLetter.toString()))
        for(ind in 0..  friends.size-1){
            val name1:Char = friends.get(ind).username.get(0)
            var name2:Char?
            if(ind!=friends.size-1){
                name2 = friends.get(ind + 1).username.get(0)
            }else{
                name2 = null
            }
            if(name1 == name2 || name2 == null ){
                val friendObj = FriendsAdapterItemClass(0, friends.get(ind))
                itemList.add(friendObj)
            }else{
                val letterObj = FriendsAdapterItemClass(1,name1.toString().uppercase())
                itemList.add(letterObj)
                val friendObj = FriendsAdapterItemClass(0, friends.get(ind))
                itemList.add(friendObj)
                if(ind+1!=friends.size){
                    itemList.add(FriendsAdapterItemClass(1, name2.toString().uppercase()))
                    itemList.add(FriendsAdapterItemClass(0,friends.get(ind+1)))
                }
            }
        }

        val friendsAdapter = FriendsAdapter(requireContext(),itemList){view,friend ->
            if(view.findViewById<CheckBox>(R.id.user_selected).isChecked){
                view.findViewById<CheckBox>(R.id.user_selected).isChecked = false
                projectViewModel.removeUser(friend)
                true
            }else if(!view.findViewById<CheckBox>(R.id.user_selected).isChecked){
                view.findViewById<CheckBox>(R.id.user_selected).isChecked = true
                projectViewModel.addUser(friend)
                true
            }
            else{
                false
            }
        }
        Log.d("SIZEARR", friendsAdapter.itemCount.toString())
        friendList.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        friendList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        friendList.adapter = friendsAdapter

        binding.skipCreateProject.setOnClickListener {

        }
    }
}