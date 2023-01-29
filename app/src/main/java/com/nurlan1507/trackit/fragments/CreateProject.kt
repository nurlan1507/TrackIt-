package com.nurlan1507.trackit.fragments

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.app.DatePickerDialog
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nurlan1507.trackit.MainActivity
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.adapter.BackGroundImagesAdapter
import com.nurlan1507.trackit.adapter.GridItemDecoration
import com.nurlan1507.trackit.data.projectBoardBackgrounds
import com.nurlan1507.trackit.databinding.FragmentCreateProjectBinding
import com.nurlan1507.trackit.viewmodels.ProjectViewModel
import java.util.*

class CreateProject : Fragment() {
    private val sharedProjectViewModel: ProjectViewModel by activityViewModels()
    private lateinit var _binding:FragmentCreateProjectBinding


    private lateinit var startDateBtn: Button
    private lateinit var endDateBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        val actionbar =(activity as AppCompatActivity).supportActionBar
        (activity as MainActivity).disableDrawer()

        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateProjectBinding.inflate(inflater,container,false)
        _binding.apply {
            viewModel = sharedProjectViewModel
        }
        val recyclerView = _binding.imageList
        val backGroundImagesAdapter = BackGroundImagesAdapter(requireContext(),projectBoardBackgrounds){
            sharedProjectViewModel.setBackground(it.id)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        recyclerView.addItemDecoration(GridItemDecoration())
        recyclerView.adapter = backGroundImagesAdapter
        return _binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val myCalendarStartDate = Calendar.getInstance()
        val myStartDatePicker = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            myCalendarStartDate.set(Calendar.YEAR, year)
            myCalendarStartDate.set(Calendar.MONTH, month)
            myCalendarStartDate.set(Calendar.DAY_OF_MONTH, day)
            sharedProjectViewModel.setStartDate(myCalendarStartDate.time.toInstant().epochSecond)
            startDateBtn.text = sharedProjectViewModel.startDate()
        }
        val startDateDialog = DatePickerDialog(requireContext(), myStartDatePicker,
            myCalendarStartDate.get(Calendar.YEAR),
            myCalendarStartDate.get(Calendar.MONTH),
            myCalendarStartDate.get(Calendar.DAY_OF_MONTH)
        )


        val myCalendarEndDate = Calendar.getInstance()
        val myEndDatePicker = DatePickerDialog.OnDateSetListener{ datePicker, year, month, day ->
            myCalendarEndDate.set(Calendar.YEAR,year)
            myCalendarEndDate.set(Calendar.MONTH,month)
            myCalendarEndDate.set(Calendar.DAY_OF_MONTH,day)
            sharedProjectViewModel.setEndDate(myCalendarEndDate.time.toInstant().epochSecond)
            endDateBtn.text = sharedProjectViewModel.endDate()
        }
        val endDateDialog = DatePickerDialog(requireContext(), myEndDatePicker,
            myCalendarEndDate.get(Calendar.YEAR),
            myCalendarEndDate.get(Calendar.MONTH),
            myCalendarEndDate.get(Calendar.DAY_OF_MONTH)
        )
        startDateBtn = _binding.startDateBtn
        endDateBtn = _binding.endDateBtn
        startDateBtn.setOnClickListener {
            startDateDialog.show()
        }
        endDateBtn.setOnClickListener {
            endDateDialog.show()
        }
        _binding.createProjectBtn.setOnClickListener {
            //validation
            if(_binding.inputProjectName.text?.isEmpty() == true){
                _binding.inputProjectName.error = "Field should not be empty"
                _binding.inputProjectName.requestFocus()
                return@setOnClickListener
            }
            if(_binding.descriptionInput.text?.isEmpty() == true){
                _binding.descriptionInput.error = "Field should not be empty"
                _binding.descriptionInput.requestFocus()
                return@setOnClickListener
            }
            if(sharedProjectViewModel.endDate() =="No date"){
                _binding.endDateBtn.requestFocus()
                _binding.dateError.text = "Pls set date"
                return@setOnClickListener
            }
            if(!sharedProjectViewModel.validateDates()){
                _binding.endDateBtn.requestFocus()
                _binding.dateError.text = "Deadline cannot be earlier than a start date"
                return@setOnClickListener
            }
            findNavController().navigate(R.id.action_createProject_to_friendListFragment)
        }
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

    }


    override fun onDestroy() {
        (activity as MainActivity).enableDrawer()
        super.onDestroy()
    }



}

