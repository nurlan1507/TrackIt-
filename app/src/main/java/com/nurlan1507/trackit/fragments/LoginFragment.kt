package com.nurlan1507.trackit.fragments

import  android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    private lateinit var _binding:FragmentLoginBinding
    val binding get() = _binding
    private lateinit var mAuth:FirebaseAuth
    private lateinit var db:FirebaseFirestore

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
        db = FirebaseFirestore.getInstance()
        binding.loginBtn.setOnClickListener { view->
            Toast.makeText(requireContext(),activity?.currentFocus?.id.toString(), Toast.LENGTH_LONG).show()
            if(activity?.currentFocus != null){
                Toast.makeText(requireContext(),activity?.currentFocus?.id.toString(), Toast.LENGTH_LONG).show()
                val service = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                service.hideSoftInputFromWindow(view.windowToken,0)
            }
        }

        binding.linkToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

}