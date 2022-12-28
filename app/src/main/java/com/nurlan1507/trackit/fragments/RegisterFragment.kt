package com.nurlan1507.trackit.fragments

import android.os.Bundle
import android.os.UserHandle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.databinding.FragmentRegisterBinding
import com.nurlan1507.trackit.repositories.AuthRepository
import com.nurlan1507.trackit.viewmodels.UserViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    private lateinit var _binding:FragmentRegisterBinding
    val binding get()= _binding
    private val viewModel:UserViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email = binding.inputEmail.text.toString()
        val password = binding.inputPassword!!.text.toString()
        val username = binding.inputUsername!!.text.toString()
        val passwordConfirmation = binding.inputPasswordConfirm!!.text.toString()
        binding.registerBtn?.setOnClickListener {
            GlobalScope.launch {
                val isSuccess = viewModel.register(email,username,password,passwordConfirmation)
                if(isSuccess){
                    findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                }
            }
        }
    }



}