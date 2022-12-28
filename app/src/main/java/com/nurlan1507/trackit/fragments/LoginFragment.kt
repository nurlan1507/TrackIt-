package com.nurlan1507.trackit.fragments

import  android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.databinding.FragmentLoginBinding
import com.nurlan1507.trackit.helpers.validateEmail
import com.nurlan1507.trackit.viewmodels.UserViewModel
import kotlinx.coroutines.*


class LoginFragment : Fragment() {
    private lateinit var _binding:FragmentLoginBinding
    val binding get() = _binding
    private val userViewModel:UserViewModel by activityViewModels()
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        userViewModel._userSignInErrorLiveData.observe(this, Observer {
            binding.authErrorMessage?.visibility = View.VISIBLE
            binding.authErrorMessage?.text = it
            Toast.makeText(requireContext(), "OPANA", Toast.LENGTH_SHORT).show()
        })

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
        binding.loginBtn.setOnClickListener { view->
            if(activity?.currentFocus != null){
                val service = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                service.hideSoftInputFromWindow(view.windowToken,0)
            }
            if(!validateEmail(binding.inputEmail)) return@setOnClickListener
            if(binding.inputPassword.text.toString().isEmpty())return@setOnClickListener
            GlobalScope.launch {
                userViewModel.login(binding.inputEmail.text.toString(),binding.inputPassword.text.toString())

            }
            Toast.makeText(requireContext(),"${auth.currentUser?.email}",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }

        binding.linkToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }


    }

}