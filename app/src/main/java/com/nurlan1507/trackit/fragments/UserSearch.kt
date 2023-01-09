package com.nurlan1507.trackit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nurlan1507.trackit.MainActivity
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.adapter.UserAdapter
import com.nurlan1507.trackit.data.User
import com.nurlan1507.trackit.databinding.FragmentUserSearchBinding


class UserSearch : Fragment() {
    private lateinit var _binding:FragmentUserSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as MainActivity).disableDrawer()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserSearchBinding.inflate(inflater,container,false)
        return _binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userAdapter = UserAdapter()
        userAdapter.submitList(listOf(User(),User()))
        _binding.listUsers.adapter = userAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).enableDrawer()
    }
}