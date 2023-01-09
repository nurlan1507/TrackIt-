package com.nurlan1507.trackit

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.PopupWindow
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
import com.nurlan1507.trackit.components.DrawerController
import com.nurlan1507.trackit.databinding.ActivityMainBinding
import com.nurlan1507.trackit.databinding.DrawerHeaderBinding
import com.nurlan1507.trackit.viewmodels.UserViewModel



class MainActivity: AppCompatActivity(), DrawerController {
    private lateinit var _binding:ActivityMainBinding
    val binding get() = _binding

    //viewmodel
    private val userViewModel:UserViewModel by viewModels()

    private lateinit var navController:NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var drawerLayout:DrawerLayout

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
                R.layout.fragment_register,
                R.layout.fragment_create_project,
            ),
            drawerLayout
        )
        toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        enableDrawer()

        setupActionBarWithNavController(navController,drawerLayout)
        navigationView.setupWithNavController(navController)



        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_logout ->{
                    userViewModel.logout()
                    navController.navigate(R.id.action_homeFragment_to_loginFragment)
                    true
                }
                R.id.nav_friends ->{
                    navController.navigate(R.id.action_homeFragment_to_userSearch)
                    true
                }
                else ->true
            }
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            androidx.appcompat.R.id.home ->{
                drawerLayout.openDrawer(GravityCompat.START)
                return true
            }
            R.id.add_project_btn ->{
                val popupMenu = PopupMenu(this, findViewById(R.id.add_project_btn))
                popupMenu.menuInflater.inflate(R.menu.menu_add,popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { item->
                    when(item.itemId){
                        R.id.menu_add_project ->{
//                            val view = layoutInflater.inflate(R.layout.create_project_popup_window,null)
//                            val window = PopupWindow(view,LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,true)
//                            window.showAtLocation(binding.navHost,Gravity.CENTER,0,0)
                            navController.navigate(R.id.action_homeFragment_to_createProject)

                        }
                        R.id.menu_add_task -> {
                            Toast.makeText(this,supportFragmentManager.backStackEntryCount,Toast.LENGTH_SHORT).show()

                        }
                        else ->{

                        }
                    }
                    true
                }
                popupMenu.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        var drawer:DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(supportFragmentManager.backStackEntryCount > 0){
            supportFragmentManager.popBackStack();
        }else {
            super.onBackPressed();
        }

    }


    override fun onNavigateUp(): Boolean {
        return findNavController(R.id.nav_host).navigateUp(appBarConfiguration) || super.onNavigateUp()
    }

    override fun disableDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        toolbar.setNavigationOnClickListener{
            onNavigateUp()
        }
    }

    override fun enableDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        var mDrawerToggle = object: ActionBarDrawerToggle(this,drawerLayout,toolbar,0,0){
            override fun onDrawerStateChanged(newState: Int) {
                val navigationDrawerBinding = DataBindingUtil.bind<DrawerHeaderBinding>(findViewById(R.id.drawer_header))
                navigationDrawerBinding?.apply {
                    userdata = userViewModel
                    super.onDrawerStateChanged(newState)
                }
            }
        }
        drawerLayout.addDrawerListener(mDrawerToggle)
        mDrawerToggle.syncState()
    }


}