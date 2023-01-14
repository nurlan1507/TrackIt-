package com.nurlan1507.trackit.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.nurlan1507.trackit.MainActivity
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.adapter.OnUserClickListener
import com.nurlan1507.trackit.adapter.UserAdapter
import com.nurlan1507.trackit.databinding.FragmentUserSearchBinding
import com.nurlan1507.trackit.helpers.ApiFailure
import com.nurlan1507.trackit.viewmodels.UserViewModel


class UserSearch : Fragment() {
    private lateinit var _binding:FragmentUserSearchBinding
    private val sharedUserViewModel: UserViewModel by activityViewModels()

    private lateinit var recyclerView:RecyclerView
    private lateinit var inputEmail:TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).disableDrawer()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserSearchBinding.inflate(inflater,container,false)
        _binding.apply {
            lifecycleOwner = this.lifecycleOwner
            viewmodel = sharedUserViewModel
        }
        return _binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userAdapter = UserAdapter(OnUserClickListener {view,user->
            val popupMenu = PopupMenu(requireContext(),view)
            popupMenu.menuInflater.inflate(R.menu.user_search_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.send_friend_request ->{
                        sharedUserViewModel.sendFriendRequest(user) { it ->
                            if (it is ApiFailure) {
                                Toast.makeText(requireContext(),it.e.message.toString() , Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        true
                    }
                    R.id.view_profile ->{
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        })
        recyclerView = _binding.listUsers
        sharedUserViewModel.userList.observe(this.viewLifecycleOwner) { list -> userAdapter.submitList(list) }
        recyclerView.adapter = userAdapter
        inputEmail = _binding.userEmailInput
        inputEmail.addTextChangedListener(object: TextWatcher{
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                sharedUserViewModel.findUsers(text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
        sharedUserViewModel.userList.observe(this.viewLifecycleOwner){list->
            if(list.isEmpty()){
                _binding.noUsers.text = "no users found"
            }else{
                _binding.noUsers.text=""
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).enableDrawer()
    }


}