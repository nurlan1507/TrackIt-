package com.nurlan1507.trackit.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.data.ProjectBackGround
import com.nurlan1507.trackit.databinding.BackgroundImagesItemBinding

class BackGroundImagesAdapter(var images:List<ProjectBackGround>, onClickListener:()->Unit):RecyclerView.Adapter<BackGroundImagesAdapter.BackGroundImageViewHolder>() {
    inner class BackGroundImageViewHolder(var binding:BackgroundImagesItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(backGround:ProjectBackGround){
            if(backGround.type ==1){
                binding.backgroundImageItem.setBackgroundColor(backGround.id)
                return
            }else{
                binding.backgroundImageItem.setImageResource(backGround.id)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackGroundImageViewHolder {
        var binding = BackgroundImagesItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BackGroundImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BackGroundImageViewHolder, position: Int) {
        holder.bind(images.get(position))
    }

    override fun getItemCount(): Int {
        return images.size
    }
}