package com.nurlan1507.trackit.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Icon
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.data.ProjectBackGround
import com.nurlan1507.trackit.databinding.BackgroundImagesItemBinding

class BackGroundImagesAdapter(var images:List<ProjectBackGround>, val onClickListener:(backGround: ProjectBackGround )->Unit):RecyclerView.Adapter<BackGroundImagesAdapter.BackGroundImageViewHolder>() {
    inner class BackGroundImageViewHolder(var binding:BackgroundImagesItemBinding):RecyclerView.ViewHolder(binding.root){
        @SuppressLint("ClickableViewAccessibility")
        fun bind(backGround:ProjectBackGround){
            binding.backgroundImageItem.setOnClickListener {
                onClickListener(backGround)
            }
            if(backGround.type ==1){
                binding.backgroundImageItem.setBackgroundColor(backGround.id)
                return
            }else{
                binding.backgroundImageItem.setImageResource(backGround.id)
//                binding.backgroundImageItem.setOnTouchListener { view, motionEvent ->
//                    when(motionEvent.action){
//                        MotionEvent.ACTION_DOWN ->{
//                            val imageView = binding.backgroundImageItem
//                            imageView.drawable.setColorFilter(0x77000000,PorterDuff.Mode.SRC_ATOP)
//                            imageView.invalidate()
//                        }
//                        MotionEvent.ACTION_UP->{
//
//                        }
//                        MotionEvent.ACTION_CANCEL -> {
//                            val imageView = binding.backgroundImageItem
//                            imageView.drawable.clearColorFilter()
//                            imageView.invalidate()
//                        }
//                    }
//                    true
//                }
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