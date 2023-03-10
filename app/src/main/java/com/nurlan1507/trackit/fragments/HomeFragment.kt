package com.nurlan1507.trackit.fragments

import android.content.ClipData.Item
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec
import com.google.android.material.progressindicator.IndeterminateDrawable
import com.google.firebase.auth.FirebaseAuth
import com.nurlan1507.trackit.MainActivity
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.adapter.GridItemDecoration
import com.nurlan1507.trackit.adapter.NotificationsAdapter
import com.nurlan1507.trackit.adapter.ProjectAdapter
import com.nurlan1507.trackit.data.notifications.FriendNotification
import com.nurlan1507.trackit.data.notifications.Notification
import com.nurlan1507.trackit.databinding.FragmentHomeBinding
import com.nurlan1507.trackit.viewmodels.NotificationViewModel
import com.nurlan1507.trackit.viewmodels.ProjectViewModel
import com.nurlan1507.trackit.viewmodels.UserViewModel

class HomeFragment : Fragment() {
    private lateinit var _binding: FragmentHomeBinding
    val binding get() = _binding
    private lateinit var mAuth:FirebaseAuth
    private val userViewModel:UserViewModel by activityViewModels()
    private val notificationsViewModel:NotificationViewModel by activityViewModels()
    private val sharedProjectViewModel:ProjectViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        val currentUserId = mAuth.currentUser?.uid.toString()
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.show()
        userViewModel.getFriends(currentUserId)
        Toast.makeText(requireContext(), userViewModel.user.value?.email.toString(),Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).disableDrawer()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).enableDrawer()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        _binding.apply {
            viewModel = userViewModel
        }

        if(mAuth.currentUser==null) {
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            (activity as AppCompatActivity).supportActionBar?.hide()
            (activity as MainActivity).disableDrawer()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).enableDrawer()


        binding.apply {
            viewModel = userViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        sharedProjectViewModel.getProjects(mAuth.currentUser?.uid.toString() ) { list->
            _binding.projectsListProgressBa.visibility = View.GONE
            val projectAdapter = ProjectAdapter(list, onClickListener ={ view, project ->
                Toast.makeText(requireContext(),"SSS",Toast.LENGTH_LONG).show()
                sharedProjectViewModel.setProject(project)
//                Toast.makeText(requireContext(), project.members[0].toString(), Toast.LENGTH_LONG).show(
                val action = HomeFragmentDirections.actionHomeFragmentToProjectFragment(project.id,project.title)
                findNavController().navigate(action)
            },
                onLongClickListener =  { it, project ->
                    val popupMenu = PopupMenu(requireContext(), it)
                    popupMenu.menuInflater.inflate(R.menu.project_long_menu, popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.leave_project ->
                                Toast.makeText(requireContext(), "Leave project", Toast.LENGTH_SHORT)
                                    .show()
                            R.id.invitation_link ->
                                Toast.makeText(requireContext(), "invitation link", Toast.LENGTH_LONG)
                                    .show()
                            R.id.add_to_favorites ->
                                Toast.makeText(requireContext(), "addtofavoriites", Toast.LENGTH_SHORT)
                                    .show()
                        }
                        true
                    }
                    if (project.title == "TrackIt!") {
                        popupMenu.menu.removeItem(R.id.add_to_favorites)
                        popupMenu.menu.add(1, R.id.add_to_favorites, 1, "remove from favorites")
                    }
                    popupMenu.show()
                }
                )
            val projectRecyclerView: RecyclerView = binding.projectsList
            projectRecyclerView.addItemDecoration(GridItemDecoration())
            projectRecyclerView.adapter = projectAdapter
        }


        val notificationRecyclerView:RecyclerView = binding.notificationList
        val itemDecor = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        ResourcesCompat.getDrawable(resources,R.drawable.divider, null)?.let {
            itemDecor.setDrawable(it)
        }
        notificationRecyclerView.addItemDecoration(itemDecor)
        val notificationAdapter = NotificationsAdapter(requireContext()){notificaiton ->
                if(notificaiton is Notification){
                    notificationsViewModel.getNotification(notificaiton.notificationId)
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToUserProfile(userId=notificaiton.sender.uid))
                }
        }
        notificationRecyclerView.adapter = notificationAdapter
        notificationsViewModel.notifications.observe(this.viewLifecycleOwner){ list ->
            if(list.isEmpty()){
                Log.d("SIZE0","no notifications yet")
            }
            notificationAdapter.submitList(list)
        }
        notificationsViewModel.getNotifications()

    }







    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).disableDrawer()
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar,menu)
        Log.d("MENUINFLATER","MENUFFF")
        super.onCreateOptionsMenu(menu, inflater)
    }




}

