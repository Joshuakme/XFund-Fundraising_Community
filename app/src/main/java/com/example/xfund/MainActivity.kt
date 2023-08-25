package com.example.xfund

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.xfund.adapter.CommunityItemAdapter
import com.example.xfund.data.CommunityDatasource
import com.example.xfund.screens.navigation.CommunityFragment
import com.example.xfund.screens.navigation.HomeFragment
import com.example.xfund.screens.navigation.ProfileFragment
import com.example.xfund.screens.navigation.ProjectsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.xfund.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
//    private lateinit var database: DatabaseReference
    lateinit var bottomNav : BottomNavigationView
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

//        // Initialize data.
//        val myDataset = CommunityDatasource().loadCommunities()
//
//        val recyclerView = findViewById<RecyclerView>(R.id.CommunityRecycleView)
//        recyclerView.adapter = CommunityItemAdapter(this, myDataset)
//
//        // Use this setting to improve performance if you know that changes
//        // in content do not change the layout size of the RecyclerView
//        recyclerView.setHasFixedSize(true)

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

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }
}
