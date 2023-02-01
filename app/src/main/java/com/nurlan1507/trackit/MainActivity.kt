package com.nurlan1507.trackit

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*

import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

import com.nurlan1507.trackit.components.DrawerController

import com.nurlan1507.trackit.databinding.ActivityMainBinding
import com.nurlan1507.trackit.databinding.DrawerHeaderBinding

import com.nurlan1507.trackit.viewmodels.UserViewModel
import java.util.concurrent.TimeUnit

val MAuth = FirebaseAuth.getInstance()
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


//        FirebaseFirestore.getInstance().collection("users").document("zvNJbKnGMMNKE10AR7g9V5KiNZO2")
//            .collection("notication").addSnapshotListener {snapshot ,_  ->
//                Toast.makeText(this,"CHANGE!",Toast.LENGTH_SHORT).show()
//                val myWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
//                    .setInitialDelay(0, TimeUnit.SECONDS)
//                    .setInputData(workDataOf("title" to "you have new request", "message" to snapshot!!.documentChanges.get(0).document.toObject(Notification::class.java).text ))
//                    .build()
//                WorkManager.getInstance(this).enqueue(myWorkRequest)
//
//            }

//
//      Firebase.messaging.subscribeToTopic(FirebaseAuth.getInstance().currentUser?.uid.toString())
//    .addOnCompleteListener { task ->
//        var msg = "Subscribed"
//        if (!task.isSuccessful) {
//            msg = "Subscribe failed"
//        }
//        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//    }
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

