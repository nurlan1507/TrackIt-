package com.nurlan1507.trackit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.data.Project
import com.nurlan1507.trackit.data.ProjectBackGround
import com.nurlan1507.trackit.databinding.CardProjectBinding

class ProjectAdapter(var onClickListener:(View,Project)->Unit): RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {
    val projects = listOf<Project>(
        Project("TrackIt!","Mobile app for tracking project progress", ProjectBackGround(),
            mutableListOf(),2,1),
        Project("ChatApp!","ChatApp for communicate with friends", ProjectBackGround(), mutableListOf(),1,2),
        Project("ChatApp!","ChatApp for communicate with friends", ProjectBackGround(), mutableListOf(),1,1),
        Project("ChatApp!","ChatApp for communicate with friends", ProjectBackGround(), mutableListOf(),1,1),
        Project("ChatApp!","ChatApp for communicate with friends", ProjectBackGround(), mutableListOf(),4,6),
        Project("ChatApp!","ChatApp for communicate with friends", ProjectBackGround(), mutableListOf(),5,5),
    )
    inner class ProjectViewHolder(val binding: CardProjectBinding):RecyclerView.ViewHolder(binding.root)

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

}
