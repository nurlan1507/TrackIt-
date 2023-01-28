package com.nurlan1507.trackit.adapter

import android.animation.ArgbEvaluator
import android.animation.LayoutTransition
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Icon
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.adapters.ImageViewBindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.data.ProjectBackGround
import com.nurlan1507.trackit.databinding.BackgroundImagesItemBinding

class BackGroundImagesAdapter(val context: Context, var images:List<ProjectBackGround>, val onClickListener:(backGround: ProjectBackGround )->Unit):RecyclerView.Adapter<BackGroundImagesAdapter.BackGroundImageViewHolder>() {
    private var checkedPosition = 0 //zero by default

    inner class BackGroundImageViewHolder(var binding:BackgroundImagesItemBinding):RecyclerView.ViewHolder(binding.root){
        @SuppressLint("ClickableViewAccessibility")
        fun bind(backGround:ProjectBackGround,position: Int){
            if(backGround.type ==1){
                binding.backgroundImageItem.setBackgroundColor(backGround.id)
                if(position == checkedPosition){
                    binding.tick.visibility = View.VISIBLE
                }else{
                    animateReverse(binding.backgroundImageItem,backGround)
                    binding.tick.visibility = View.GONE
                }
            }else{
                binding.backgroundImageItem.setImageResource(backGround.id)
                if(position == checkedPosition){
                    binding.tick.visibility = View.VISIBLE
                }else{
                    animateReverse(binding.backgroundImageItem,backGround)
                    binding.tick.visibility = View.GONE
                }

            }


            binding.root.setOnClickListener {
                if(position == checkedPosition){
                    return@setOnClickListener
                }
                setCheckedPosition(position)
                notifyDataSetChanged()
                onClickListener(backGround)
                animate(binding.backgroundImageItem)

                Log.d("AdapterPosition", adapterPosition.toString())
            }
        }

        fun setImageSelected(imageId:Int){

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackGroundImageViewHolder {
        var binding = BackgroundImagesItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        binding.root.layoutTransition.enableTransitionType(LayoutTransition.APPEARING)
        return BackGroundImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BackGroundImageViewHolder, position: Int) {
        holder.bind(images[position],position)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    private fun setCheckedPosition(pos:Int){
        checkedPosition = pos
    }


    private fun animate(imageView:ImageView){
        val colorFrom:Int
        if(imageView.drawable!=null){
            colorFrom = context.resources.getColor(R.color.transparent)
        }else{
            colorFrom = (imageView.background as ColorDrawable).color
        }
        val colorTo = context.resources.getColor(R.color.item_selected)
        val anim = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom,colorTo).apply {
            duration = 450
        }
        anim.start()

        anim.addUpdateListener {
            if(imageView.drawable!=null){
                imageView.drawable.setColorFilter(it.getAnimatedValue() as Int, PorterDuff.Mode.SRC_ATOP)
            }else{
                imageView.setBackgroundColor(it.getAnimatedValue() as Int)
            }
        }
    }

    private fun animateReverse(imageView:ImageView, backGround: ProjectBackGround){
        val colorTo:Int
        var colorFrom = context.resources.getColor(R.color.item_selected)
        if(imageView.drawable!= null){
            colorTo = context.resources.getColor(R.color.transparent)
        }else{
            colorTo = backGround.id
            colorFrom = (imageView.background as ColorDrawable).color
        }
        val anim = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom,colorTo).apply {
            duration = 450
        }
        anim.start()

        anim.addUpdateListener {
            if(imageView.drawable!=null)  imageView.setColorFilter(it.getAnimatedValue() as Int)
            else imageView.setBackgroundColor(it.getAnimatedValue() as Int)
        }
    }
}