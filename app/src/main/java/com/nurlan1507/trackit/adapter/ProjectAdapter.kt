package com.nurlan1507.trackit.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.data.Project
import com.nurlan1507.trackit.data.ProjectBackGround
import com.nurlan1507.trackit.databinding.CardProjectBinding

class ProjectAdapter(var projects:List<Project>, var onClickListener:(View,Project)->Unit): RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    inner class ProjectViewHolder(val binding: CardProjectBinding):RecyclerView.ViewHolder(binding.root)
    init{
        Log.d("SIZE", this.itemCount.toString())
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val binding: CardProjectBinding = CardProjectBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ProjectViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        with(holder){
            binding.projectTitle.text = projects.get(position).title
            binding.cardProjectImage.setImageResource(projects.get(position).image.id)
        }
        holder.itemView.setOnLongClickListener{
            onClickListener(it,projects.get(position))
            true
        }

        holder.binding.starProjectBtn?.setOnClickListener {
            onClickListener(it,projects.get(position))
            true
        }
    }

    override fun getItemCount(): Int {
        return projects.size
    }

    fun setData(projectList:List<Project>){
        projects = projectList
    }
}
