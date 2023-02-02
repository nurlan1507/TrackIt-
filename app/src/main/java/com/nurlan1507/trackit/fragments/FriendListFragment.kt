package com.nurlan1507.trackit.fragments

import android.graphics.PorterDuff
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec
import com.google.android.material.progressindicator.IndeterminateDrawable
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.adapter.FriendsAdapter
import com.nurlan1507.trackit.adapter.FriendsAdapterItemClass
import com.nurlan1507.trackit.data.Project
import com.nurlan1507.trackit.data.User
import com.nurlan1507.trackit.databinding.FragmentFriendListBinding
import com.nurlan1507.trackit.utils.ProjectWorker
import com.nurlan1507.trackit.viewmodels.ProjectViewModel
import com.nurlan1507.trackit.viewmodels.UserViewModel


class FriendListFragment : Fragment() {
    private lateinit var binding:FragmentFriendListBinding
    private val userViewModel: UserViewModel by activityViewModels()
    private val projectViewModel:ProjectViewModel by activityViewModels()
    private lateinit var friendList:RecyclerView
    private var friends:MutableList<User> = mutableListOf()



    private lateinit var createProjectBtn: MaterialButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        friends = userViewModel._friends.value!!
        Log.d("FRLOX", friends.size.toString())
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
        createProjectBtn = binding.skipCreateProject

        friends.sortWith(Comparator {o1:User, o2:User ->
            o1.username.compareTo(o2.username)
        })
        val itemList:ArrayList<FriendsAdapterItemClass> = arrayListOf()
        if(friends.size!=0){
            var firstLetter = friends[0].username.get(0)
            itemList.add(FriendsAdapterItemClass(1,firstLetter.toString()))
            itemList.add(FriendsAdapterItemClass(0, friends[0]))
            for(ind in 0 until friends.size){
                val name1:Char = friends.get(ind).username.get(0)
                var name2:Char?
                if(ind!=friends.size-1){
                    name2 = friends.get(ind + 1).username.get(0)
                }else{
                    name2 = null
                }
                if((name1 == name2 || name2 == null ) && (ind != friends.size -1)){
                    val friendObj = FriendsAdapterItemClass(0, friends.get(ind))
                    itemList.add(friendObj)
                }else{
//                val letterObj = FriendsAdapterItemClass(1,name1.toString().uppercase())
//                itemList.add(letterObj)
//                val friendObj = FriendsAdapterItemClass(0, friends.get(ind))
//                itemList.add(friendObj)
                    if(ind+1!=friends.size){
                        itemList.add(FriendsAdapterItemClass(1, name2.toString().uppercase()))
                        itemList.add(FriendsAdapterItemClass(0,friends.get(ind+1)))
                    }
                }
            }
        }


        val friendsAdapter = FriendsAdapter(requireContext(),itemList){view,friend ->
            if(view.findViewById<CheckBox>(R.id.user_selected).isChecked){
                view.findViewById<CheckBox>(R.id.user_selected).isChecked = false
                projectViewModel.removeUser(friend.uid)
                true
            }else if(!view.findViewById<CheckBox>(R.id.user_selected).isChecked){
                view.findViewById<CheckBox>(R.id.user_selected).isChecked = true
                projectViewModel.addUser(friend.uid)
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

        createProjectBtn.setOnClickListener {
            val icon = createProjectBtn.icon
            val spec = CircularProgressIndicatorSpec(requireContext(), null,0,
                com.google.android.material.R.style.Widget_Material3_CircularProgressIndicator_ExtraSmall)
            val progressIndicator = IndeterminateDrawable.createCircularDrawable(requireContext(),spec)
            progressIndicator.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)
            createProjectBtn.icon = progressIndicator


            projectViewModel.createProject(){
                val workManager = WorkManager.getInstance(requireContext())
                val request = OneTimeWorkRequestBuilder<ProjectWorker>()
                    .setInputData(workDataOf("memberList" to it.members.toTypedArray(),"projectId" to it.id))
                    .build()
                workManager.enqueue(request)
                createProjectBtn.icon = icon
                val action = FriendListFragmentDirections.actionFriendListFragmentToProjectFragment(it.id, it.title)
                findNavController().navigate(action)
            }


        }
    }
}