package com.nurlan1507.trackit.fragments

import android.R.attr.animation
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.PopupWindow
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.nurlan1507.trackit.MainActivity
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
        (activity as MainActivity).disableDrawer()
        setHasOptionsMenu(true)

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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar_project, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.add_a_task ->{
                createPopupWindow()
                true
            }else->{
                true
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).disableDrawer()
    }


    private fun createPopupWindow(){
//       val popupIntent = Intent(requireContext(),TaskPopupWindow::class.java)
//        startActivity(popupIntent)
        val popupView = layoutInflater.inflate(R.layout.task_popup_window, null)
        val popupBackground = popupView.findViewById<ConstraintLayout>(R.id.popup_window_background)
        val popupWindowView = popupView.findViewById<CardView>(R.id.popup_window_view_with_border)
        val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true)


        val alpha = 100 //between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), Color.TRANSPARENT, alphaColor)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            popupBackground.setBackgroundColor(animator.animatedValue as Int)
        }



        popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0 ,0)

        popupWindowView.animate().alpha(1f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()
        colorAnimation.start()

        fun onPopupClose() {
            val exitAnimation = AnimationUtils.loadAnimation(requireContext(),R.anim.popup_exit)
            exitAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}

                override fun onAnimationEnd(animation: Animation) {
                    popupWindow.dismiss()
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
            popupWindow.contentView.startAnimation(exitAnimation)

        }



        popupBackground.setOnClickListener {
            onPopupClose()
        }


    }
    class PopupCloseWorker(ctx: Context, attr:WorkerParameters, var popupWindow:PopupWindow ):Worker(ctx,attr){
        override fun doWork(): Result {
            popupWindow.dismiss()
            return Result.success()
        }

    }

}