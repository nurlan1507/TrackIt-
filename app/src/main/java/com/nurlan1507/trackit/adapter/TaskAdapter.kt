package com.nurlan1507.trackit.adapter

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.nurlan1507.trackit.data.Task
import com.nurlan1507.trackit.databinding.ItemTaskBinding

class TaskAdapter(var itemList:List<Task>,val onClickListener:()->Unit ):RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    inner class TaskViewHolder(val binding:ItemTaskBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(task:Task){
            binding.root.setOnClickListener{
                if(binding.hiddenView.visibility == View.GONE){
                    TransitionManager.beginDelayedTransition(binding.hiddenView, AutoTransition())
                    binding.hiddenView.visibility = View.VISIBLE
                }
            }
            binding.hideButton.setOnClickListener {
                if(binding.hiddenView.visibility == View.VISIBLE){
                    TransitionManager.beginDelayedTransition(binding.hiddenView, AutoTransition())
                    binding.hiddenView.visibility = View.GONE
                }
            }
            binding.taskTitle.text = task.title
            binding.daysLeft.text = task.startDate.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {

    }
}