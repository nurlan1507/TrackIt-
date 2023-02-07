package com.nurlan1507.trackit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.adapter.TaskAdapter
import com.nurlan1507.trackit.data.Task
import com.nurlan1507.trackit.databinding.FragmentProjectBinding
import com.nurlan1507.trackit.viewmodels.ProjectViewModel


class ProjectFragment : Fragment() {
    private lateinit var  binding: FragmentProjectBinding
    private val sharedProjectViewModel:ProjectViewModel by activityViewModels()
    private lateinit var taskRecyclerView:RecyclerView
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
        taskRecyclerView = binding.taskList
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = listOf(Task("1","Title","Description","ss", System.currentTimeMillis(), System.currentTimeMillis(), listOf()))
        val taskAdapter = TaskAdapter(list){

        }
        taskRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        taskRecyclerView.adapter = taskAdapter
    }


}