package com.nurlan1507.trackit.fragments

import android.os.Bundle
import android.os.UserHandle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.nurlan1507.trackit.databinding.FragmentRegisterBinding
import com.nurlan1507.trackit.repositories.AuthRepository
import com.nurlan1507.trackit.viewmodels.UserViewModel

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
        binding.registerBtn?.setOnClickListener {
            val email = binding.inputEmail
            val password = binding.inputPassword!!
            val username = binding.inputUsername!!
            val passwordConfirmation = binding.inputPasswordConfirm!!
            viewModel.register(email,username,password,passwordConfirmation)

        }
    }

}