package com.nurlan1507.trackit.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.impl.utils.taskexecutor.WorkManagerTaskExecutor
import androidx.work.workDataOf
import com.bumptech.glide.Glide
import com.nurlan1507.trackit.MainActivity
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.databinding.FragmentUserProfileBinding
import com.nurlan1507.trackit.utils.FriendWorker
import com.nurlan1507.trackit.viewmodels.NotificationViewModel
import com.nurlan1507.trackit.viewmodels.UserViewModel
import kotlin.math.atan


class UserProfile : Fragment() {
    private lateinit var args: UserProfileArgs
    private val userViewModel: UserViewModel by activityViewModels()
    private val notificationViewModel :NotificationViewModel by activityViewModels()
    private lateinit var _binding:FragmentUserProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).disableDrawer()
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        args = arguments.let {
            UserProfileArgs.fromBundle(it!!)
        }
        _binding = FragmentUserProfileBinding.inflate(inflater,container,false)
        _binding.apply {
            notificationVM = notificationViewModel
        }
        return _binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding.userProfileLin.setOnHoverListener { view, motionEvent ->
            Toast.makeText(requireContext(),"фывфыв", Toast.LENGTH_LONG).show()
            true
        }
        val userId = args.userId
        Toast.makeText(requireContext(),(userId==userViewModel.user.value?.uid).toString(),Toast.LENGTH_SHORT).show()
        if(userId!=null && userId == userViewModel.user.value?.uid){
            _binding.email.text = userViewModel.user.value?.email
            _binding.username.text = userViewModel.user.value?.username
            Glide.with(requireContext()).load(userViewModel.user.value?.avatarUrl).into(_binding.userAvatar)
        }else{
            userViewModel.getUser(userId!!){ user ->
                _binding.email.text = user.email
                _binding.username.text = user.username
                _binding.phone.text = "no phone set"
                Glide.with(requireContext()).load(user.avatarUrl).into(_binding.userAvatar)
            }
        }
        _binding.btnFriendRequest.setOnClickListener {
            acceptFriend()
            notificationViewModel.deleteNotification(userViewModel.user.value?.uid.toString())
        }
    }

    fun acceptFriend(){
        userViewModel.acceptFriend(args.userId.toString()){
            Toast.makeText(requireContext(), "${_binding.username.text} is now your friend", Toast.LENGTH_SHORT).show()
            val req = OneTimeWorkRequestBuilder<FriendWorker>().setInputData(workDataOf("senderId" to args.userId,"receiverId" to userViewModel.user.value?.uid.toString())).build()
            notificationViewModel.deleteNotification(args.userId.toString())
            WorkManager.getInstance(requireContext()).enqueue(req)
        }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_user_profile_toolbar, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.user_profile_more_action_btn ->{
                val popupMenu = PopupMenu(requireContext(),(activity as MainActivity).findViewById(R.id.user_profile_more_action_btn))
                if(args.userId == null){
                    popupMenu.menuInflater.inflate(R.menu.menu_my_profile, popupMenu.menu)
                }else{
                    popupMenu.menu.add("Block")
                    val isFriend = userViewModel.isFriend(args.userId.toString())
                    if(!isFriend)
                        popupMenu.menu.add("Add to friend")
                    else
                        popupMenu.menu.add("Remove a friend")
                }
                popupMenu.show()
                true
            }
            else -> {
                false
            }
        }
    }



}

