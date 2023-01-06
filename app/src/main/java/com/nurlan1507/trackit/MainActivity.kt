package com.nurlan1507.trackit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.nurlan1507.trackit.databinding.ActivityMainBinding
import com.nurlan1507.trackit.databinding.DrawerHeaderBinding
import com.nurlan1507.trackit.fragments.HomeFragment
import com.nurlan1507.trackit.viewmodels.UserViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var _binding:ActivityMainBinding
    val binding get() = _binding

    //viewmodel
    private val userViewModel:UserViewModel by viewModels()

    private lateinit var navController:NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var drawerLayout:DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        drawerLayout = binding.drawerLayout
        navigationView = binding.navView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController


        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.layout.fragment_home,
                R.layout.fragment_login,
                R.layout.fragment_register
            ),
            drawerLayout
        )
        toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        val mDrawerToggle = object: ActionBarDrawerToggle(this,drawerLayout,toolbar,0,0){
            override fun onDrawerStateChanged(newState: Int) {
                val navigationDrawerBinding = DataBindingUtil.bind<DrawerHeaderBinding>(findViewById(R.id.drawer_header))
                navigationDrawerBinding?.apply {
                    userdata = userViewModel
                }
                super.onDrawerStateChanged(newState)
            }
        }
        drawerLayout.addDrawerListener(mDrawerToggle)
        mDrawerToggle.syncState()
        setupActionBarWithNavController(navController,drawerLayout)
        navigationView.setupWithNavController(navController)



        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_logout ->{
                    userViewModel.logout()
                    navController.navigate(R.id.action_homeFragment_to_loginFragment)
                    Toast.makeText(this,"LOL",Toast.LENGTH_LONG).show()
                    true
                }
                else ->true
            }
        }


        _binding.toolbarAddBtn.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.menuInflater.inflate(R.menu.menu_add,popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item->
                when(item.itemId){
                    R.id.menu_add_project ->{
                        Toast.makeText(this,"LOL",Toast.LENGTH_LONG).show()
                    }
                    R.id.menu_add_task -> {

                    }
                    else ->{

                    }
                }
                true
            }
            popupMenu.show()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            androidx.appcompat.R.id.home ->{
                drawerLayout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onNavigateUp(): Boolean {
        return findNavController(R.id.nav_host).navigateUp(appBarConfiguration) || super.onNavigateUp()
    }




}