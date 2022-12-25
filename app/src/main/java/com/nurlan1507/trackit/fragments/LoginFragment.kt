package com.nurlan1507.trackit.fragments

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.nurlan1507.trackit.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    private lateinit var _binding:FragmentLoginBinding
    val binding get() = _binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.inputEmail.setOnKeyListener { view, keyEvent,_->
//            val service = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            if(keyEvent == KeyEvent.KEYCODE_ENTER)
//                service.hideSoftInputFromWindow(view.windowToken,0)
//            true
//        }

        binding.loginBtn.setOnClickListener {
                view->
            if(activity?.currentFocus != null){
                Toast.makeText(requireContext(),activity?.currentFocus?.id.toString(), Toast.LENGTH_LONG).show()
                val service = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                service.hideSoftInputFromWindow(view.windowToken,0)
            }

        }
    }

}