package com.example.xfund

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.xfund.databinding.ActivityMainBinding
import com.example.xfund.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
//    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    lateinit var bottomNav : BottomNavigationView
    lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var currentUserViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Initialize Firebase
        auth = Firebase.auth


        // Initialize data.
        bottomNav = binding.bottomNav
        val navHostFragment = supportFragmentManager.findFragmentById(binding.myNavHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController
        currentUserViewModel = ViewModelProvider(this)[UserViewModel::class.java]


        // Initialize Bottom Nav Item Badges
        setNavItemBadge(R.id.profileFragment)
        setNavItemBadge(R.id.communityFragment)


        // Bottom Nav Controller
        bottomNav.setupWithNavController(navController)

//        // Event Listener -> BottomNav Items
//        bottomNav.setOnItemSelectedListener {item ->
//            when (item.itemId) {
//                R.id.homeFragment -> {
//                    removeNavItemBadgeOnClick(item.itemId)
//                    true
//                }
//
//                R.id.projectsFragment -> {
//                    removeNavItemBadgeOnClick(item.itemId)
//                    true
//                }
//
//                R.id.communityFragment -> {
//                    removeNavItemBadgeOnClick(item.itemId)
//
//                    true
//                }
//
//                R.id.profileFragment -> {
//                    removeNavItemBadgeOnClick(item.itemId)
//                    true
//                }
//
//                else -> {false}
//            }
//        }
    }


    private fun setNavItemBadge(menuItemId : Int) {
        var badge = bottomNav.getOrCreateBadge(menuItemId)
        badge.isVisible = true
        // An icon only badge will be displayed unless a number is set:
        //badge.number = 99
    }

    private fun removeNavItemBadgeOnClick(menuItemId : Int) {
        // Item Badge
        val homeBadgeDrawable = bottomNav.getBadge(menuItemId)
        if (homeBadgeDrawable != null) {
            homeBadgeDrawable.isVisible = false
            homeBadgeDrawable.clearNumber()
        }
    }
}
