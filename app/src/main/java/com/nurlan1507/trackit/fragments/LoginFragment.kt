package com.nurlan1507.trackit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    private lateinit var _binding:FragmentLoginBinding
    val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        return _binding.root
    }

}