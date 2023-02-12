package com.nurlan1507.trackit.adapter

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.data.Task
import com.nurlan1507.trackit.databinding.ItemTaskBinding

class TaskAdapter(var itemList:List<Task>,val onClickListener:()->Unit ):RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    inner class TaskViewHolder(val binding:ItemTaskBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(task:Task){
            val cardView = binding.taskCard
            val hiddenView = binding.hiddenView
//            binding.hideButton.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
            binding.taskInfoConstraintLayout.setOnClickListener {
                if(hiddenView.visibility == View.GONE){
                    TransitionManager.beginDelayedTransition(cardView, AutoTransition())
                    hiddenView.visibility = View.VISIBLE
                }else{
                    TransitionManager.beginDelayedTransition(cardView, AutoTransition())
                    hiddenView.visibility = View.GONE
                }
            }
//            binding.hideButton.setOnClickListener {
//                if(binding.hiddenView.visibility == View.VISIBLE){
//                    TransitionManager.beginDelayedTransition(cardView, AutoTransition())
//                    hiddenView.visibility = View.GONE
//                }
//            }
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
        holder.bind(itemList.get(position))
    }
}