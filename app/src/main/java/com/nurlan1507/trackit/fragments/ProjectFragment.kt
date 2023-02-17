package com.nurlan1507.trackit.fragments

import android.R.attr.*
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.net.IpSecManager.SpiUnavailableException
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.material.textfield.TextInputEditText
import com.nurlan1507.trackit.MainActivity
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.adapter.MembersAdapter
import com.nurlan1507.trackit.adapter.TaskAdapter
import com.nurlan1507.trackit.data.Task
import com.nurlan1507.trackit.data.User
import com.nurlan1507.trackit.databinding.FragmentProjectBinding
import com.nurlan1507.trackit.viewmodels.ProjectViewModel
import com.nurlan1507.trackit.viewmodels.TaskViewModel
import org.w3c.dom.Text
import java.util.*


class ProjectFragment : Fragment() {
    private lateinit var  binding: FragmentProjectBinding
    private val sharedProjectViewModel:ProjectViewModel by activityViewModels()
    private val sharedTaskViewModel:TaskViewModel by activityViewModels()
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
        sharedTaskViewModel.initiateTask()

        val popupView = layoutInflater.inflate(R.layout.task_popup_window, null)
        val popupBackground = popupView.findViewById<ConstraintLayout>(R.id.popup_window_background)
        val popupWindowView = popupView.findViewById<CardView>(R.id.popup_window_view_with_border)
        val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true)

        //task data
        val titleView: TextInputEditText = popupView.findViewById<TextInputEditText>(R.id.input_task_name)
        val startDateBtn: Button = popupView.findViewById(R.id.task_start_date_btn)
        val endDateBtn:Button = popupView.findViewById(R.id.task_end_date_btn)
        val createButton:Button = popupView.findViewById(R.id.create_task_btn)
        val memberList: MutableList<User> = mutableListOf()

        createButton.setOnClickListener {
            if(titleView.text?.isEmpty() == true){
                titleView.error = "Title can not be empty"
                titleView.requestFocus()
            }
            sharedTaskViewModel.createTask(titleView.text.toString(),"description", memberList)
        }
        startDateBtn.setOnClickListener {
            startDatePickerDialog()
        }

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


        val spinner = popupView.findViewById<Spinner>(R.id.spinner)
        val userAdapter = MembersAdapter(requireContext(),sharedProjectViewModel.project.value!!.members){view->
            val checkBox = view.findViewById<CheckBox>(R.id.member_selected)
            if(!checkBox.isChecked){
                checkBox.isChecked=true
                false
            }else{
                checkBox.isChecked = false
                true
            }
        }
        spinner.adapter = userAdapter
        spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                p3: Long
            ) {
                val user = spinner.adapter.getItem(position) as User
                if(memberList.contains(user)){
                    memberList.remove(user)
                }else{
                    memberList.add(user)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }



        }



//        spinner.prompt = "Promtp"
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

    private fun startDatePickerDialog() {
        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(requireContext(), "Pick a date", Toast.LENGTH_SHORT).show()
        }
        val negativeButtonClick = { dialog: DialogInterface, which: Int ->
            dialog.dismiss()
        }
        val dialog = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.task_date_picker_dialog, null)
        val startDateSpinner = view.findViewById<Button>(R.id.task_start_date_spinner)
        val startTimeSpinner = view.findViewById<Spinner>(R.id.task_start_time_spinner)
        val endDateSpinner = view.findViewById<Button>(R.id.task_end_date_spinner)
        val endTimeSpinner = view.findViewById<Spinner>(R.id.task_end_time_spinner)

        var timeAdapter = TimeAdapter(requireContext(),resources.getStringArray(R.array.time_arr))
        startTimeSpinner.adapter = timeAdapter
        endTimeSpinner.adapter = timeAdapter
        //start
        startDateSpinner.setOnClickListener {
            showStartDatePickerDialog(startTimeSpinner)
        }
        startTimeSpinner.onItemSelectedListener = object :OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                sharedTaskViewModel.setStartTime((p1 as TextView).text)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        //end
        endDateSpinner.setOnClickListener {
            showEndDatePickerDialog(endTimeSpinner)
        }
        endTimeSpinner.onItemSelectedListener = object:OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                sharedTaskViewModel.setEndTime((p1 as TextView).text.toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


        with(dialog) {
            setTitle("Pick a date")
            setPositiveButton("Set date", DialogInterface.OnClickListener(positiveButtonClick))
            setNegativeButton("Back", DialogInterface.OnClickListener(negativeButtonClick))
            setView(view)
            show()
        }

    }

    private fun showStartDatePickerDialog(spinner:Spinner){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(requireContext())
        datePickerDialog.setOnDateSetListener { view, year, monnth, dayOfMonth ->
            calendar.set(Calendar.YEAR,year)
            calendar.set(Calendar.MONTH,month)
            calendar.set(Calendar.DAY_OF_MONTH,day)
            sharedTaskViewModel.setStartDate(calendar.time.toInstant().toEpochMilli(),resources.getStringArray(R.array.time_arr)[spinner.selectedItemPosition] )
        }
        datePickerDialog.show()
    }
    private fun showEndDatePickerDialog(spinner:Spinner){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(requireContext())
        datePickerDialog.setOnDateSetListener { view, year, monnth, dayOfMonth ->
            calendar.set(Calendar.YEAR,year)
            calendar.set(Calendar.MONTH,month)
            calendar.set(Calendar.DAY_OF_MONTH,day)
            sharedTaskViewModel.setEndDate(calendar.time.toInstant().toEpochMilli(),resources.getStringArray(R.array.time_arr)[spinner.selectedItemPosition] )

        }
        var a = resources.getStringArray(R.array.time_arr)

        datePickerDialog.show()
    }

    class TimeAdapter(var ctx: Context, var arr :Array<String>):ArrayAdapter<String>(ctx,android.R.layout.simple_spinner_item,arr){
        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            val textView = TextView(ctx)
            textView.setPadding(30,20,30,20)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            textView.text = arr.get(position)
            return textView
        }
    }


}
    class PopupCloseWorker(ctx: Context, attr:WorkerParameters, var popupWindow:PopupWindow ):Worker(ctx,attr){
        override fun doWork(): Result {
            popupWindow.dismiss()
            return Result.success()
        }

    }

