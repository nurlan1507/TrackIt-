package com.nurlan1507.trackit.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.databinding.FragmentRegisterBinding
import com.nurlan1507.trackit.helpers.validateEmail
import com.nurlan1507.trackit.helpers.validateUsername
import com.nurlan1507.trackit.viewmodels.UserViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    private lateinit var _binding:FragmentRegisterBinding
    private val binding get()= _binding
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

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.registerBtn.setOnClickListener {
            if(!validateEmail(binding.inputEmail)) return@setOnClickListener
            if(binding.inputPassword.text.toString().isEmpty())return@setOnClickListener
            if(binding.inputPassword.text.toString() != binding.inputPasswordConfirm.text.toString()){
                binding.inputPassword.error = "password do not match"
                binding.inputPassword.requestFocus()
                binding.inputPassword.text = null
                binding.inputPasswordConfirm.text = null
                return@setOnClickListener
            }
            if(!validateUsername(binding.inputUsername))return@setOnClickListener
            GlobalScope.launch(Dispatchers.IO) {
                viewModel.register( binding.inputEmail.text.toString(),binding.inputUsername.text.toString() ,binding.inputPassword.text.toString(),binding.inputPasswordConfirm.text.toString())
            }
            findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
        }

    }



}