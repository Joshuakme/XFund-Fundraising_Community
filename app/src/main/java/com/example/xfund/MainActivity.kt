package com.example.xfund

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.xfund.screens.navigation.CommunityFragment
import com.example.xfund.screens.navigation.HomeFragment
import com.example.xfund.screens.navigation.ProfileFragment
import com.example.xfund.screens.navigation.ProjectsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
//    private lateinit var database: DatabaseReference
        lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadFragment(HomeFragment())
        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }

                R.id.projects -> {
                    loadFragment(ProjectsFragment())
                    true
                }

                R.id.community -> {
                    loadFragment(CommunityFragment())
                    true
                }

                R.id.profile -> {
                    loadFragment(ProfileFragment())
                    true
                }

                else -> {false}
            }
        }
    }

//    fun writeNewUser(userId: String, name: String, email: String) {
//        database = Firebase.database.reference
//
//        val user = User(name, email)
//
//        database.child("users").child(userId).setValue(user)
//    }

    fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }
}
