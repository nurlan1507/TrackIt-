package com.nurlan1507.trackit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.databinding.FragmentHomeBinding
import com.nurlan1507.trackit.viewmodels.UserViewModel

class HomeFragment : Fragment() {
    private lateinit var _binding: FragmentHomeBinding
    val binding get() = _binding
    private lateinit var mAuth:FirebaseAuth
    private val userViewModel:UserViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        (activity as AppCompatActivity).supportActionBar?.show()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = userViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        binding.logoutBtn.setOnClickListener {
            if(mAuth.currentUser!=null){
                mAuth.signOut()
            }else{
                Toast.makeText(requireContext(),"You are already signed Oot!",Toast.LENGTH_SHORT ).show()
            }
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            return@setOnClickListener
        }
    }

}