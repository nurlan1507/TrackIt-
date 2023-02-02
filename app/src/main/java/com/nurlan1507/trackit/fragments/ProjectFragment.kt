package com.nurlan1507.trackit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.databinding.FragmentProjectBinding
import com.nurlan1507.trackit.viewmodels.ProjectViewModel


class ProjectFragment : Fragment() {
    private lateinit var  binding: FragmentProjectBinding
    private val sharedProjectViewModel:ProjectViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProjectBinding.inflate(inflater,container,false)
        binding.apply {
            viewLifecycleOwner
            projectViewModel = sharedProjectViewModel
        }
        return binding.root
    }

}