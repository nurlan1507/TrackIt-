package com.nurlan1507.trackit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.nurlan1507.trackit.databinding.ActivityMainBinding
import com.nurlan1507.trackit.fragments.HomeFragment


class MainActivity : AppCompatActivity() {
    private lateinit var _binding:ActivityMainBinding
    val binding get() = _binding
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
        val mDrawerToggle = ActionBarDrawerToggle(this,drawerLayout,toolbar,0,0)
        drawerLayout.addDrawerListener(mDrawerToggle)
        mDrawerToggle.syncState()
        setupActionBarWithNavController(navController,drawerLayout)
        navigationView.setupWithNavController(navController)

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

    fun enableBurger(){
        val mDrawerToggle = ActionBarDrawerToggle(this,drawerLayout,toolbar,0,0)
        drawerLayout.addDrawerListener(mDrawerToggle)
        val currentFragment = supportFragmentManager.findFragmentById(R.id.nav_host)
        if(currentFragment is HomeFragment){
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            mDrawerToggle.isDrawerIndicatorEnabled = true
            mDrawerToggle.isDrawerSlideAnimationEnabled = true
            mDrawerToggle.toolbarNavigationClickListener = null
            drawerLayout.addDrawerListener(mDrawerToggle)
            mDrawerToggle.syncState()
        }else{
            mDrawerToggle.isDrawerIndicatorEnabled = false
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

        }
    }


    override fun onNavigateUp(): Boolean {
        return findNavController(R.id.nav_host).navigateUp(appBarConfiguration) || super.onNavigateUp()
    }


}