package com.nurlan1507.trackit.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.nurlan1507.trackit.MainActivity
import com.nurlan1507.trackit.R
import com.nurlan1507.trackit.adapter.GridItemDecoration
import com.nurlan1507.trackit.adapter.ProjectAdapter
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
        if(mAuth.currentUser==null) {
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            (activity as AppCompatActivity).supportActionBar?.hide()
            (activity as MainActivity).disableDrawer()
        }
        (activity as AppCompatActivity).supportActionBar?.show()

        Toast.makeText(requireContext(), userViewModel.user.value?.email.toString(),Toast.LENGTH_SHORT).show()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        _binding.apply {
            viewModel = userViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).enableDrawer()



        binding.apply {
            viewModel = userViewModel
            lifecycleOwner = viewLifecycleOwner
        }




        val recyclerView:RecyclerView = binding.projectsList
        recyclerView.addItemDecoration(GridItemDecoration())
        recyclerView.adapter = ProjectAdapter {
            it,project->
            val popupMenu = PopupMenu(requireContext(),it)
            popupMenu.menuInflater.inflate(R.menu.project_long_menu,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.leave_project ->
                        Toast.makeText(requireContext(),"Leave project", Toast.LENGTH_SHORT).show()
                    R.id.invitation_link ->
                        Toast.makeText(requireContext(),"invitation link", Toast.LENGTH_LONG).show()
                    R.id.add_to_favorites ->
                        Toast.makeText(requireContext(),"addtofavoriites", Toast.LENGTH_SHORT).show()
                }
                true
            }
            if(project.title == "TrackIt!"){
                popupMenu.menu.removeItem(R.id.add_to_favorites)
                popupMenu.menu.add(1, R.id.add_to_favorites, 1  ,"remove from favorites")
            }
            popupMenu.show()
        }


    }


    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.leave_project ->
                Toast.makeText(requireContext(),"Leave project", Toast.LENGTH_SHORT).show()
            R.id.invitation_link ->
                Toast.makeText(requireContext(),"invitation link", Toast.LENGTH_LONG).show()
            R.id.add_to_favorites ->
                Toast.makeText(requireContext(),"addtofavoriites", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).disableDrawer()
    }

}
